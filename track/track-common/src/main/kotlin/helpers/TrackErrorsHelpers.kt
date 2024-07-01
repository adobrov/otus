package ru.otus.otuskotlin.track.common.helpers


import ru.otus.otuskotlin.track.common.models.TrackError

fun Throwable.asTrackError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = TrackError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)