package ru.otus.otuskotlin.track.biz.validation

import ru.otus.otuskotlin.track.common.models.TrackCommand
import kotlin.test.Test

class BizValidationReadTest: BaseBizValidationTest() {
    override val command = TrackCommand.READ

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)

}