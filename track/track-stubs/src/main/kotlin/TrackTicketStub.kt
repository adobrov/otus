package ru.otus.otuskotlin.track.stubs

import ru.otus.otuskotlin.track.common.models.TrackTicket
import ru.otus.otuskotlin.track.common.models.TrackTicketId

import ru.otus.otuskotlin.track.stubs.TrackTicketStubTicket.SIMPLE_TICKET1
// import ru.otus.otuskotlin.track.stubs.MkplAdStubBolts.SIMPLE_TICKET2

object TrackTicketStub {
    fun get(): TrackTicket = SIMPLE_TICKET1.copy()

    fun prepareResult(block: TrackTicket.() -> Unit): TrackTicket = get().apply(block)
    fun prepareSearchList(filter: String) = mutableListOf(
        TrackTicket(TrackTicketId(666), subject = "Заявка", description = "Заявка"),
        TrackTicket(TrackTicketId(777), subject = "Заявка2", description = "Заявка2"),

    )
}
