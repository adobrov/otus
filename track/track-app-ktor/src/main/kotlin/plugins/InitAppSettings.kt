package ru.otus.otuskotlin.track.app.ktor.plugins

import io.ktor.server.application.*
import ru.otus.otuskotlin.track.app.ktor.TrackAppSettings
import ru.otus.otuskotlin.track.biz.TrackTicketProcessor
import ru.otus.otuskotlin.track.common.TrackCorSettings
import ru.otus.otuskotlin.track.app.ktor.base.KtorWsSessionRepo

fun Application.initAppSettings(): TrackAppSettings {
    val corSettings = TrackCorSettings(
        loggerProvider = getLoggerProviderConf(),
        wsSessions = KtorWsSessionRepo(),
    )
    return TrackAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = TrackTicketProcessor(corSettings),
    )
}