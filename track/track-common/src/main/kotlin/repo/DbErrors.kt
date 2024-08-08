package ru.otus.otuskotlin.track.common.repo

import ru.otus.otuskotlin.track.common.helpers.errorSystem
import ru.otus.otuskotlin.track.common.models.TrackTicketId
import ru.otus.otuskotlin.track.common.models.TrackError
import ru.otus.otuskotlin.track.common.models.TrackTicket
import ru.otus.otuskotlin.track.common.models.TrackTicketLock
import ru.otus.otuskotlin.track.common.repo.exceptions.RepoConcurrencyException
import ru.otus.otuskotlin.track.common.repo.exceptions.RepoException

const val ERROR_GROUP_REPO = "repo"

fun errorNotFound(id: TrackTicketId) = DbTicketResponseErr(
    TrackError(
        code = "$ERROR_GROUP_REPO-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Object with ID: ${id.asInt()} is not Found",
    )
)

val errorEmptyId = DbTicketResponseErr(
    TrackError(
        code = "$ERROR_GROUP_REPO-empty-id",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Id must not be null or blank"
    )
)

fun errorRepoConcurrency(
    oldTicket: TrackTicket,
    expectedLock: TrackTicketLock,
    exception: Exception = RepoConcurrencyException(
        id = oldTicket.id,
        expectedLock = expectedLock,
        actualLock = oldTicket.lock,
    ),
) = DbTicketResponseErrWithData(
    ticket = oldTicket,
    err = TrackError(
        code = "$ERROR_GROUP_REPO-concurrency",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "The object with ID ${oldTicket.id.asInt()} has been changed concurrently by another user or process",
        exception = exception,
    )
)

fun errorEmptyLock(id: TrackTicketId) = DbTicketResponseErr(
    TrackError(
        code = "$ERROR_GROUP_REPO-lock-empty",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "Lock for Ad ${id.asInt()} is empty that is not admitted"
    )
)

fun errorDb(e: RepoException) = DbTicketResponseErr(
    errorSystem(
        violationCode = "dbLockEmpty",
        e = e
    )
)