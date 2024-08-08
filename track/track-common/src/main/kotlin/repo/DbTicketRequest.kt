package ru.otus.otuskotlin.track.common.repo

import ru.otus.otuskotlin.track.common.models.TrackTicket

data class DbTicketRequest(
    val ticket: TrackTicket
)