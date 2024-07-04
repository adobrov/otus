package ru.otus.otuskotlin.track.biz.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.models.TrackTicketFilter
import ru.otus.otuskotlin.track.common.models.TrackCommand
import ru.otus.otuskotlin.track.common.models.TrackOperationState
import ru.otus.otuskotlin.track.common.models.TrackWorkMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizValidationSearchTest: BaseBizValidationTest() {
    override val command = TrackCommand.SEARCH

    @Test
    fun correctEmpty() = runTest {
        val ctx = TrackContext(
            command = command,
            operationState = TrackOperationState.NONE,
            workMode = TrackWorkMode.TEST,
            ticketFilterRequest = TrackTicketFilter()
        )
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertNotEquals(TrackOperationState.FAILING, ctx.operationState)
    }
}