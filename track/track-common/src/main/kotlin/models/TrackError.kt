package ru.otus.otuskotlin.track.common.models

import ru.otus.otuskotlin.track.logging.common.LogLevel

data class TrackError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val level: LogLevel = LogLevel.ERROR,
    val exception: Throwable? = null,
)