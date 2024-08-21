package ru.otus.otuskotlin.track.common.repo.exceptions

import ru.otus.otuskotlin.track.common.models.TrackTicketId

open class RepoTicketException(
    @Suppress("unused")
    val ticketId: TrackTicketId,
    msg: String,
): RepoException(msg)