package ru.otus.otuskotlin.track.common.models


data class TrackTicketFilter(
    var searchString: String = "",
    var ownerId: TrackOwnerId = TrackOwnerId.NONE,
    var state: TrackState = TrackState.NONE,
)