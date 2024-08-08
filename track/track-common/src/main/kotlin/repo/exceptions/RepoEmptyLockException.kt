package ru.otus.otuskotlin.track.common.repo.exceptions

import ru.otus.otuskotlin.track.common.models.TrackTicketId

class RepoEmptyLockException(id: TrackTicketId): RepoTicketException(
    id,
    "Lock is empty in DB"
)