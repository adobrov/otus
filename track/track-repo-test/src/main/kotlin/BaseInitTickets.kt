package ru.otus.otuskotlin.track.backend.repo.tests

import ru.otus.otuskotlin.track.common.models.*
import ru.otus.otuskotlin.track.common.models.TrackOwnerId
import ru.otus.otuskotlin.track.common.models.TrackTicket

abstract class BaseInitTickets(private val i: Int): IInitObjects<TrackTicket> {
    fun createInitTestModel(
        suf: String,
        ownerId: TrackOwnerId = TrackOwnerId("owner-123"),
    ) = TrackTicket(
        id = TrackTicketId(i),
        subject = "$suf stub",
        state = TrackState.PROGRESS,
        description = "$suf stub description",
        owner = ownerId,
    )
}