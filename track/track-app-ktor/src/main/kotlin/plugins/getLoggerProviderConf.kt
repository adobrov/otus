package ru.otus.otuskotlin.track.app.ktor.plugins

import io.ktor.server.application.*
import ru.otus.otuskotlin.track.logging.common.TrackLoggerProvider
import ru.otus.otuskotlin.track.logging.logback.tLoggerLogback


fun Application.getLoggerProviderConf(): TrackLoggerProvider =
    when (val mode = environment.config.propertyOrNull("ktor.logger")?.getString()) {
        "logback", null -> TrackLoggerProvider { tLoggerLogback(it) }
        else -> throw Exception("Logger $mode is not allowed.")
}
