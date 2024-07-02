package ru.otus.otuskotlin.track.app.ktor

import ru.otus.otuskotlin.track.app.common.ITrackAppSettings
import ru.otus.otuskotlin.track.biz.TrackTicketProcessor
import ru.otus.otuskotlin.track.common.TrackCorSettings

data class TrackAppSettings(
    val appUrls: List<String> = emptyList(),
    override val corSettings: TrackCorSettings = TrackCorSettings(),
    override val processor: TrackTicketProcessor = TrackTicketProcessor(corSettings),
): ITrackAppSettings