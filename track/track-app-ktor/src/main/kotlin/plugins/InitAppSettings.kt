package ru.otus.otuskotlin.track.app.ktor.plugins

import io.ktor.server.application.Application
import ru.otus.otuskotlin.track.app.ktor.TrackAppSettings
import ru.otus.otuskotlin.track.biz.TrackTicketProcessor
import ru.otus.otuskotlin.track.common.TrackCorSettings
import ru.otus.otuskotlin.track.app.ktor.base.KtorWsSessionRepo
import ru.otus.otuskotlin.track.backend.repository.inmemory.TicketRepoStub
import ru.otus.otuskotlin.track.repo.inmemory.TicketRepoInMemory

fun Application.initAppSettings(): TrackAppSettings {
    val corSettings = TrackCorSettings(
        loggerProvider = getLoggerProviderConf(),
        wsSessions = KtorWsSessionRepo(),
        repoTest = TicketRepoInMemory(),
        repoProd = TicketRepoInMemory(),
        repoStub = TicketRepoStub(),
    )
    return TrackAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = TrackTicketProcessor(corSettings),
    )
}