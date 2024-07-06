package ru.otus.otuskotlin.track.biz.validation

import ru.otus.otuskotlin.track.biz.TrackTicketProcessor
import ru.otus.otuskotlin.track.common.TrackCorSettings
import ru.otus.otuskotlin.track.common.models.TrackCommand

abstract class BaseBizValidationTest {
    protected abstract val command: TrackCommand
    private val settings by lazy { TrackCorSettings() }
    protected val processor by lazy { TrackTicketProcessor(settings) }
}