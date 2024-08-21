package ru.otus.otuskotlin.track.common.repo

interface IRepoTicket {
    suspend fun createTicket(rq: DbTicketRequest): IDbTicketResponse
    suspend fun readTicket(rq: DbTicketIdRequest): IDbTicketResponse
    suspend fun updateTicket(rq: DbTicketRequest): IDbTicketResponse
    suspend fun deleteTicket(rq: DbTicketIdRequest): IDbTicketResponse
    suspend fun searchTicket(rq: DbTicketFilterRequest): IDbTicketsResponse
    companion object {
        val NONE = object : IRepoTicket {
            override suspend fun createTicket(rq: DbTicketRequest): IDbTicketResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun readTicket(rq: DbTicketIdRequest): IDbTicketResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun updateTicket(rq: DbTicketRequest): IDbTicketResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun deleteTicket(rq: DbTicketIdRequest): IDbTicketResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun searchTicket(rq: DbTicketFilterRequest): IDbTicketsResponse {
                throw NotImplementedError("Must not be used")
            }
        }
    }
}