package ru.otus.otuskotlin.track.biz.validation

import ru.otus.otuskotlin.track.common.models.TrackCommand
import kotlin.test.Test

class BizValidationUpdateTest: BaseBizValidationTest() {
    override val command = TrackCommand.UPDATE

    @Test fun correctTitle() = validationTitleCorrect(command, processor)
    @Test fun trimTitle() = validationTitleTrim(command, processor)
    @Test fun emptyTitle() = validationTitleEmpty(command, processor)
    @Test fun badSymbolsTitle() = validationTitleSymbols(command, processor)

    @Test fun correctDescription() = validationDescriptionCorrect(command, processor)
    @Test fun trimDescription() = validationDescriptionTrim(command, processor)
    @Test fun emptyDescription() = validationDescriptionEmpty(command, processor)
    @Test fun badSymbolsDescription() = validationDescriptionSymbols(command, processor)

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun correctLock() = validationLockCorrect(command, processor)
    @Test fun emptyLock() = validationLockEmpty(command, processor)

}