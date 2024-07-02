package ru.otus.otuskotlin.track.common

import ru.otus.otuskotlin.track.logging.common.TrackLoggerProvider
import ru.otus.otuskotlin.track.common.ws.ITrackWsSessionRepo

data class TrackCorSettings(
    val loggerProvider: TrackLoggerProvider = TrackLoggerProvider(),
    val wsSessions: ITrackWsSessionRepo = ITrackWsSessionRepo.NONE,
) {
    companion object {
        val NONE = TrackCorSettings()
    }
}