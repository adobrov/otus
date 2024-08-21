package ru.otus.otuskotlin.track.backend.repo.tests

import ru.otus.otuskotlin.track.common.models.TrackTicket
import ru.otus.otuskotlin.track.common.repo.*

class TicketRepositoryMock(
    private val invokeCreateTicket: (DbTicketRequest) -> IDbTicketResponse = { DEFAULT_TICKET_SUCCESS_EMPTY_MOCK },
    private val invokeReadTicket: (DbTicketIdRequest) -> IDbTicketResponse = { DEFAULT_TICKET_SUCCESS_EMPTY_MOCK },
    private val invokeUpdateTicket: (DbTicketRequest) -> IDbTicketResponse = { DEFAULT_TICKET_SUCCESS_EMPTY_MOCK },
    private val invokeDeleteTicket: (DbTicketIdRequest) -> IDbTicketResponse = { DEFAULT_TICKET_SUCCESS_EMPTY_MOCK },
    private val invokeSearchTicket: (DbTicketFilterRequest) -> IDbTicketsResponse = { DEFAULT_TICKETS_SUCCESS_EMPTY_MOCK },
): IRepoTicket {
    override suspend fun createTicket(rq: DbTicketRequest): IDbTicketResponse {
        return invokeCreateTicket(rq)
    }

    override suspend fun readTicket(rq: DbTicketIdRequest): IDbTicketResponse {
        return invokeReadTicket(rq)
    }

    override suspend fun updateTicket(rq: DbTicketRequest): IDbTicketResponse {
        return invokeUpdateTicket(rq)
    }

    override suspend fun deleteTicket(rq: DbTicketIdRequest): IDbTicketResponse {
        return invokeDeleteTicket(rq)
    }

    override suspend fun searchTicket(rq: DbTicketFilterRequest): IDbTicketsResponse {
        return invokeSearchTicket(rq)
    }

    companion object {
        val DEFAULT_TICKET_SUCCESS_EMPTY_MOCK = DbTicketResponseOk(TrackTicket())
        val DEFAULT_TICKETS_SUCCESS_EMPTY_MOCK = DbTicketsResponseOk(emptyList())
    }
}