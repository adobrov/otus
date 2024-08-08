package ru.otus.otuskotlin.track.common.repo.exceptions

import ru.otus.otuskotlin.track.common.models.TrackTicketId
import ru.otus.otuskotlin.track.common.models.TrackTicketLock

class RepoConcurrencyException(id: TrackTicketId, expectedLock: TrackTicketLock, actualLock: TrackTicketLock?): RepoTicketException(
    id,
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)
