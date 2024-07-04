package ru.otus.otuskotlin.track.biz.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.track.biz.TrackTicketProcessor
import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationIdCorrect(command: TrackCommand, processor: TrackTicketProcessor) = runTest {
    val ctx = TrackContext(
        command = command,
        operationState = TrackOperationState.NONE,
        workMode = TrackWorkMode.TEST,
        ticketRequest = TrackTicket(
            id = TrackTicketId(1),
            subject = "abc",
            description = "abc",
            lock = TrackTicketLock(1),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(TrackOperationState.FAILING, ctx.operationState)
}

fun validationIdEmpty(command: TrackCommand, processor: TrackTicketProcessor) = runTest {
    val ctx = TrackContext(
        command = command,
        operationState = TrackOperationState.NONE,
        workMode = TrackWorkMode.TEST,
        ticketRequest = TrackTicket(
            id = TrackTicketId(0),
            subject = "abc",
            description = "abc",
            lock = TrackTicketLock(1),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(TrackOperationState.FAILING, ctx.operationState)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}
