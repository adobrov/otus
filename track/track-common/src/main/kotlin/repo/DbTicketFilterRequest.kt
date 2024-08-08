package ru.otus.otuskotlin.track.common.repo

import ru.otus.otuskotlin.track.common.models.TrackOwnerId

data class DbTicketFilterRequest(
    val titleFilter: String = "",
    val ownerId: TrackOwnerId = TrackOwnerId.NONE,
)