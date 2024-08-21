package ru.otus.otuskotlin.track.backend.repo.postgresql

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.otus.otuskotlin.track.common.helpers.asTrackError
import ru.otus.otuskotlin.track.common.models.*
import ru.otus.otuskotlin.track.common.repo.*
import ru.otus.otuskotlin.track.repo.common.IRepoTicketInitializable

class RepoTicketSql (
    properties: SqlProperties,
) : IRepoTicket, IRepoTicketInitializable {

    private val ticketTable = TicketTable("${properties.schema}.${properties.ticketTable}")
    private val commentTable = CommentTable("${properties.schema}.${properties.commentTable}")
    private val rnd = (0..1000).random()

    private val driver = when {
        properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
        else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
    }

    private val conn = Database.connect(
        "jdbc:postgresql://127.0.0.1:5432/track", driver, properties.user, properties.password
    )

    fun clear(): Unit = transaction(conn) {
        commentTable.deleteAll()
        ticketTable.deleteAll()
    }

    private fun saveObj(t: TrackTicket): TrackTicket = transaction(conn) {
        val res = ticketTable
            .insert {
                to(it, t)
            }
            .resultedValues
            ?.map { ticketTable.from(it) }
        for (comments in t.comments) {
            commentTable.insert {
                to(it, comments)
            }
        }
        res?.first() ?: throw RuntimeException("BD error: insert statement returned empty result")
    }

    private suspend inline fun <T> transactionWrapper(crossinline block: () -> T, crossinline handle: (Exception) -> T): T =
        withContext(Dispatchers.IO) {
            try {
                transaction(conn) {
                    block()
                }
            } catch (e: Exception) {
                handle(e)
            }
        }

    private suspend inline fun transactionWrapper(crossinline block: () -> IDbTicketResponse): IDbTicketResponse =
        transactionWrapper(block) { DbTicketResponseErr(it.asTrackError()) }

    override fun save(tickets: Collection<TrackTicket>): Collection<TrackTicket> = tickets.map { saveObj(it) }

    override suspend fun createTicket(rq: DbTicketRequest): IDbTicketResponse = transactionWrapper {
        DbTicketResponseOk(saveObj(rq.ticket))
    }

    private fun read(id: TrackTicketId): IDbTicketResponse {
        val tRes = ticketTable.selectAll().where {
            ticketTable.id eq id.asInt()
        }.singleOrNull() ?: return errorNotFound(id)

        val cRes = commentTable.selectAll().where {
            commentTable.ticket_id eq id.asInt()
        }.orderBy(commentTable.id)

        val ticket = ticketTable.from(tRes)
        for (r in cRes) {
            ticket.comments.addLast(commentTable.from(r))
        }
        return DbTicketResponseOk(ticket)
    }

    override suspend fun readTicket(rq: DbTicketIdRequest): IDbTicketResponse = transactionWrapper { read(rq.id) }

    private suspend fun update(
        id: TrackTicketId,
        lock: TrackTicketLock,
        block: (TrackTicket) -> IDbTicketResponse
    ): IDbTicketResponse =
        transactionWrapper {
            if (id == TrackTicketId.NONE) return@transactionWrapper errorEmptyId

            val current = ticketTable.selectAll().where { ticketTable.id eq id.asInt() }
                .singleOrNull()
                ?.let { ticketTable.from(it) }

            when {
                current == null -> errorNotFound(id)
                current.lock != lock -> errorRepoConcurrency(current, lock)
                else -> block(current)
            }
        }


    override suspend fun updateTicket(rq: DbTicketRequest): IDbTicketResponse = update(rq.ticket.id, rq.ticket.lock) {
        ticketTable.update({ ticketTable.id eq rq.ticket.id.asInt() }) {
            to(it, rq.ticket.copy(lock = TrackTicketLock(rnd)))
        }
        read(rq.ticket.id)
    }

    override suspend fun deleteTicket(rq: DbTicketIdRequest): IDbTicketResponse = update(rq.id, rq.lock) {
        ticketTable.deleteWhere { id eq rq.id.asInt() }
        DbTicketResponseOk(it)
    }

    override suspend fun searchTicket(rq: DbTicketFilterRequest): IDbTicketsResponse =
        transactionWrapper({
            val res = ticketTable.selectAll().where {
                buildList {
                    add(Op.TRUE)
                    if (rq.ownerId != TrackOwnerId.NONE) {
                        add(ticketTable.owner eq rq.ownerId.asString())
                    }
                    if (rq.titleFilter.isNotBlank()) {
                        add(
                            (ticketTable.subject like "%${rq.titleFilter}%")
                                    or (ticketTable.description like "%${rq.titleFilter}%")
                        )
                    }
                }.reduce { a, b -> a and b }
            }
            DbTicketsResponseOk(data = res.map { ticketTable.from(it) })
        }, {
            DbTicketsResponseErr(it.asTrackError())
        })
}