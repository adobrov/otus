package ru.otus.otuskotlin.track.stubs

import kotlinx.datetime.*
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.track.common.models.*

object TrackTicketStubTicket {

    val SIMPLE_TICKET1: TrackTicket
        get() = TrackTicket(
            id = TrackTicketId(666),
            subject = "Заявка",
            description = "Проблема",
            owner = TrackOwnerId("user-1"),
            state = TrackState.NEW,
            creationDate = LocalDateTime.parse("2012-01-01T10:00:00").toInstant(TimeZone.UTC),
            lock = TrackTicketLock(5),
        )
    val SIMPLE_TICKET2 = SIMPLE_TICKET1.copy(
        subject = "Заявка2"
    )
}
