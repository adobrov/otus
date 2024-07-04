package ru.otus.otuskotlin.track.biz.validation

import ru.otus.otuskotlin.track.common.models.TrackCommand
import kotlin.test.Test

class BizValidationDeleteTest: BaseBizValidationTest() {
    override val command = TrackCommand.DELETE

    @Test
    fun correctId() = validationIdCorrect(command, processor)
    @Test
    fun emptyId() = validationIdEmpty(command, processor)

    @Test
    fun correctLock() = validationLockCorrect(command, processor)
    @Test
    fun emptyLock() = validationLockEmpty(command, processor)

}
