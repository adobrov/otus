package ru.otus.otuskotlin.track.repo.inmemory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.otus.otuskotlin.track.common.helpers.errorSystem
import ru.otus.otuskotlin.track.common.models.*
import ru.otus.otuskotlin.track.common.repo.*
import ru.otus.otuskotlin.track.repo.common.IRepoTicketInitializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class TicketRepoInMemory(
    ttl: Duration = 2.minutes,
    val randomUuid: () -> Int = { (0..10000).random() },
) : TicketRepoBase(), IRepoTicket, IRepoTicketInitializable {

    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<Int, TicketEntity>()
        .expireAfterWrite(ttl)
        .build()

    override fun save(ads: Collection<TrackTicket>) = ads.map { ticket ->
        val entity = TicketEntity(ticket)
        require(entity.id != null)
        cache.put(entity.id, entity)
        ticket
    }

    override suspend fun createTicket(rq: DbTicketRequest): IDbTicketResponse = tryTicketMethod {
        val key = randomUuid()
        val ad = rq.ticket.copy(id = TrackTicketId(key))
        val entity = TicketEntity(ad)
        mutex.withLock {
            cache.put(key, entity)
        }
        DbTicketResponseOk(ad)
    }

    override suspend fun readTicket(rq: DbTicketIdRequest): IDbTicketResponse = tryTicketMethod {
        val key = rq.id.takeIf { it != TrackTicketId.NONE }?.asInt() ?: return@tryTicketMethod errorEmptyId
        mutex.withLock {
            cache.get(key)
                ?.let {
                    DbTicketResponseOk(it.toInternal())
                } ?: errorNotFound(rq.id)
        }
    }

    override suspend fun updateTicket(rq: DbTicketRequest): IDbTicketResponse = tryTicketMethod {
        val rqAd = rq.ticket
        val id = rqAd.id.takeIf { it != TrackTicketId.NONE } ?: return@tryTicketMethod errorEmptyId
        val key = id.asInt()

        mutex.withLock {
            val oldTicket = cache.get(key)?.toInternal()
            when {
                oldTicket == null -> errorNotFound(id)
                else -> {
                    val newAd = rqAd.copy()
                    val entity = TicketEntity(newAd)
                    cache.put(key, entity)
                    DbTicketResponseOk(newAd)
                }
            }
        }
    }


    override suspend fun deleteTicket(rq: DbTicketIdRequest): IDbTicketResponse = tryTicketMethod {
        val id = rq.id.takeIf { it != TrackTicketId.NONE } ?: return@tryTicketMethod errorEmptyId
        val key = id.asInt()

        mutex.withLock {
            val oldAd = cache.get(key)?.toInternal()
            when {
                oldAd == null -> errorNotFound(id)
                else -> {
                    cache.invalidate(key)
                    DbTicketResponseOk(oldAd)
                }
            }
        }
    }

    /**
     * Поиск объявлений по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun searchTicket(rq: DbTicketFilterRequest): IDbTicketsResponse = tryTicketsMethod {
        val result: List<TrackTicket> = cache.asMap().asSequence()
            .filter { entry ->
                rq.ownerId.takeIf { it != TrackOwnerId.NONE }?.let {
                    it.asString() == entry.value.owner
                } ?: true
            }
            .filter { entry ->
                rq.titleFilter.takeIf { it.isNotBlank() }?.let {
                    entry.value.subject?.contains(it) ?: false
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        DbTicketsResponseOk(result)
    }
}