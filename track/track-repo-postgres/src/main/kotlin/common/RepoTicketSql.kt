package ru.otus.otuskotlin.track.backend.repo.postgresql

import ru.otus.otuskotlin.track.common.models.TrackTicket
import ru.otus.otuskotlin.track.common.repo.*
import ru.otus.otuskotlin.track.repo.common.IRepoTicketInitializable

abstract class AbsRepoTicketSql(
    properties: SqlProperties,
) : IRepoTicket, IRepoTicketInitializable {
//    override fun save(tickets: Collection<TrackTicket>): Collection<TrackTicket> {
//        throw NotImplementedError()
//    }
//    override suspend fun createTicket(rq: DbTicketRequest): IDbTicketResponse {
//        throw NotImplementedError()
//    }
//    override suspend fun readTicket(rq: DbTicketIdRequest): IDbTicketResponse {
//        throw NotImplementedError()
//    }
//    override suspend fun updateTicket(rq: DbTicketRequest): IDbTicketResponse {
//        throw NotImplementedError()
//    }
//    override suspend fun deleteTicket(rq: DbTicketIdRequest): IDbTicketResponse {
//        throw NotImplementedError()
//    }
//    override suspend fun searchTicket(rq: DbTicketFilterRequest): IDbTicketsResponse {
//        throw NotImplementedError()
//    }
//    fun clear() {
//        throw NotImplementedError()
//    }
    abstract override fun save(tickets: Collection<TrackTicket>): Collection<TrackTicket>
    abstract override suspend fun createTicket(rq: DbTicketRequest): IDbTicketResponse
    abstract override suspend fun readTicket(rq: DbTicketIdRequest): IDbTicketResponse
    abstract override suspend fun updateTicket(rq: DbTicketRequest): IDbTicketResponse
    abstract override suspend fun deleteTicket(rq: DbTicketIdRequest): IDbTicketResponse
    abstract override suspend fun searchTicket(rq: DbTicketFilterRequest): IDbTicketsResponse
    abstract fun clear()
}