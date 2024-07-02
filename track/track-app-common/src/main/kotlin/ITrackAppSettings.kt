package ru.otus.otuskotlin.track.app.common

import ru.otus.otuskotlin.track.biz.TrackTicketProcessor
import ru.otus.otuskotlin.track.common.TrackCorSettings

interface ITrackAppSettings {
    val processor: TrackTicketProcessor
    val corSettings: TrackCorSettings
}
