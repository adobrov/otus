package ru.otus.otuskotlin.track.common.models

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.track.common.NONE

data class TrackTicket(
    var id: TrackTicketId = TrackTicketId.NONE,
    var subject: String = "",
    var description: String = "",
    var owner: TrackOwnerId = TrackOwnerId.NONE,
    var state: TrackState = TrackState.NEW,
    var creationDate: Instant = Instant.NONE,
    var finishDate: Instant = Instant.NONE,
    var lock: TrackTicketLock = TrackTicketLock.NONE,
    val comments: MutableList<TrackTicketComment> = mutableListOf(),
) {
    fun isEmpty() = this == NONE

    fun deepCopy(): TrackTicket = copy(
        comments = comments.toMutableList(),
    )

    companion object {
        private val NONE = TrackTicket()
    }
}