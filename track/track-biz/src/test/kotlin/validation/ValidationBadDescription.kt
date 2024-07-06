package ru.otus.otuskotlin.track.biz.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.track.biz.TrackTicketProcessor
import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.models.*
import ru.otus.otuskotlin.track.stubs.TrackTicketStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = TrackTicketStub.get()

fun validationDescriptionCorrect(command: TrackCommand, processor: TrackTicketProcessor) = runTest {
    val ctx = TrackContext(
        command = command,
        operationState = TrackOperationState.NONE,
        workMode = TrackWorkMode.TEST,
        ticketRequest = TrackTicket(
            id = stub.id,
            subject = "abc",
            description = "abc",
            lock = TrackTicketLock(1),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(TrackOperationState.FAILING, ctx.operationState)
    assertEquals("abc", ctx.ticketValidated.description)
}

fun validationDescriptionTrim(command: TrackCommand, processor: TrackTicketProcessor) = runTest {
    val ctx = TrackContext(
        command = command,
        operationState = TrackOperationState.NONE,
        workMode = TrackWorkMode.TEST,
        ticketRequest = TrackTicket(
            id = stub.id,
            subject = "abc",
            description = " \n\tabc \n\t",
            lock = TrackTicketLock(1),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(TrackOperationState.FAILING, ctx.operationState)
    assertEquals("abc", ctx.ticketValidated.description)
}

fun validationDescriptionEmpty(command: TrackCommand, processor: TrackTicketProcessor) = runTest {
    val ctx = TrackContext(
        command = command,
        operationState = TrackOperationState.NONE,
        workMode = TrackWorkMode.TEST,
        ticketRequest = TrackTicket(
            id = stub.id,
            subject = "abc",
            description = "",
            lock = TrackTicketLock(1),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(TrackOperationState.FAILING, ctx.operationState)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}

fun validationDescriptionSymbols(command: TrackCommand, processor: TrackTicketProcessor) = runTest {
    val ctx = TrackContext(
        command = command,
        operationState = TrackOperationState.NONE,
        workMode = TrackWorkMode.TEST,
        ticketRequest = TrackTicket(
            id = stub.id,
            subject = "abc",
            description = "!@#$%^&*(),.{}",
            lock = TrackTicketLock(1),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(TrackOperationState.FAILING, ctx.operationState)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}