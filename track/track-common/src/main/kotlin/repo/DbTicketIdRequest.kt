package ru.otus.otuskotlin.track.common.repo

import ru.otus.otuskotlin.track.common.models.TrackTicket
import ru.otus.otuskotlin.track.common.models.TrackTicketId
import ru.otus.otuskotlin.track.common.models.TrackTicketLock

data class DbTicketIdRequest(
    val id: TrackTicketId,
    val lock: TrackTicketLock = TrackTicketLock.NONE,
) {
    constructor(ticket: TrackTicket): this(ticket.id, ticket.lock)
}