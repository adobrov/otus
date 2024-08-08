package ru.otus.otuskotlin.track.common.repo

import ru.otus.otuskotlin.track.common.helpers.errorSystem
abstract class TicketRepoBase: IRepoTicket {

    protected suspend fun tryTicketMethod(block: suspend () -> IDbTicketResponse) = try {
        block()
    } catch (e: Throwable) {
        DbTicketResponseErr(errorSystem("methodException", e = e))
    }

    protected suspend fun tryTicketsMethod(block: suspend () -> IDbTicketsResponse) = try {
        block()
    } catch (e: Throwable) {
        DbTicketsResponseErr(errorSystem("methodException", e = e))
    }
}