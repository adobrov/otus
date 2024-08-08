package ru.otus.otuskotlin.track.backend.repository.inmemory

import ru.otus.otuskotlin.track.common.repo.*
import ru.otus.otuskotlin.track.stubs.TrackTicketStub

class TicketRepoStub() : IRepoTicket {
    override suspend fun createTicket(rq: DbTicketRequest): IDbTicketResponse {
        return DbTicketResponseOk(
            data = TrackTicketStub.get(),
        )
    }

    override suspend fun readTicket(rq: DbTicketIdRequest): IDbTicketResponse {
        return DbTicketResponseOk(
            data = TrackTicketStub.get(),
        )
    }

    override suspend fun updateTicket(rq: DbTicketRequest): IDbTicketResponse {
        return DbTicketResponseOk(
            data = TrackTicketStub.get(),
        )
    }

    override suspend fun deleteTicket(rq: DbTicketIdRequest): IDbTicketResponse {
        return DbTicketResponseOk(
            data = TrackTicketStub.get(),
        )
    }

    override suspend fun searchTicket(rq: DbTicketFilterRequest): IDbTicketsResponse {
        return DbTicketsResponseOk(
            data = TrackTicketStub.prepareSearchList(filter = ""),
        )
    }
}