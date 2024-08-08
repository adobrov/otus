package ru.otus.otuskotlin.track.backend.repo.postgresql

import kotlinx.datetime.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import ru.otus.otuskotlin.track.common.NONE
import ru.otus.otuskotlin.track.common.models.*

class TicketTable(tableName: String) : Table(tableName) {
    val id = integer(TicketSqlFields.ID).autoIncrement()
    val subject = text(TicketSqlFields.SUBJECT).nullable()
    val description = text(TicketSqlFields.DESCRIPTION).nullable()
    val owner = text(TicketSqlFields.OWNER_ID)
    val state = ticketStateEnumeration(TicketSqlFields.STATE)
    val creationDate = datetime(TicketSqlFields.CREATION_DATE)
    val finishDate = datetime(TicketSqlFields.FINISH_DATE).nullable()
    val lock = integer(TicketSqlFields.LOCK)

    override val primaryKey = PrimaryKey(id)

    fun from(res: ResultRow) = TrackTicket(
        id = TrackTicketId(res[id].toInt()),
        subject = res[subject] ?: "",
        description = res[description] ?: "",
        owner = TrackOwnerId(res[owner].toString()),
        state = res[state],
        creationDate = res[creationDate].toInstant(TimeZone.UTC),
        finishDate = res[finishDate].takeIf { it != null} ?.toInstant(TimeZone.UTC) ?: Instant.NONE,
        lock = TrackTicketLock(res[lock].toInt()),
    )

    fun to(it: UpdateBuilder<*>, ticket: TrackTicket) {
        //it[id] = ticket.id.asInt()
        it[subject] = ticket.subject
        it[description] = ticket.description
        it[owner] = ticket.owner.asString()
        it[state] = ticket.state
        it[creationDate] = ticket.creationDate.toLocalDateTime(TimeZone.UTC)
        it[finishDate] = ticket.finishDate.takeIf { it != Instant.NONE } ?.toLocalDateTime(TimeZone.UTC)
        it[lock] = ticket.lock.asInt()
    }
}

class CommentTable(tableName: String) : Table(tableName) {
    val id = integer(CommentSqlFields.ID).autoIncrement()
    val ticket_id = integer(CommentSqlFields.TICKET_ID)
    val author = text(CommentSqlFields.AUTHOR)
    val creationDate = datetime(CommentSqlFields.CREATION_DATE)
    val text = text(CommentSqlFields.TEXT)

    override val primaryKey = PrimaryKey(id)

    fun from(res: ResultRow) = TrackTicketComment(
        id = res[id].toInt(),
        ticketId = TrackTicketId(res[id].toInt()),
        author = res[author],
        creationDate =res[creationDate].toInstant(TimeZone.UTC),
        text = res[text],
    )

    fun to(it: UpdateBuilder<*>, comment: TrackTicketComment) {
        //it[id] = comment.id
        it[ticket_id] = comment.ticketId.asInt()
        it[author] = comment.author
        it[creationDate] = comment.creationDate.toLocalDateTime(TimeZone.UTC)
        it[text] = comment.text
    }
}