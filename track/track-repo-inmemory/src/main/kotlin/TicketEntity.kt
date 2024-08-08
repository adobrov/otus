package ru.otus.otuskotlin.track.repo.inmemory

import ru.otus.otuskotlin.track.common.models.*

data class TicketEntity(
    val id: Int? = null,
    val subject: String? = null,
    val description: String? = null,
    val owner: String? = null,
    val state: TrackState? = null,
    val lock: Int? = null,
) {
    constructor(model: TrackTicket): this(
        id = model.id.asInt(),
        subject = model.subject.takeIf { it.isNotBlank() },
        description = model.description.takeIf { it.isNotBlank() },
        owner = model.owner.asString().takeIf { it.isNotBlank() },
        state = TrackState.PROGRESS,
        lock = model.lock.asInt()
    )

    fun toInternal() = TrackTicket(
        id = id?.let { TrackTicketId(it) }?: TrackTicketId.NONE,
        subject = subject?: "",
        description = description?: "",
        owner = owner?.let { TrackOwnerId(it) }?: TrackOwnerId.NONE,
        state = state?.let { TrackState.PROGRESS }?: TrackState.NONE,
        lock = lock?.let { TrackTicketLock(it) } ?: TrackTicketLock.NONE,
    )
}