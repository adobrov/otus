package ru.otus.otuskotlin.track.common.models

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.track.common.NONE

data class TrackTicketComment (
    var id: Int = 0,
    var ticketId: TrackTicketId = TrackTicketId.NONE,
    var author: String = "",
    var creationDate: Instant = Instant.NONE,
    var text: String = "",
) {
    fun isEmpty() = this == NONE

    companion object {
        val NONE = TrackTicketComment()
    }
}