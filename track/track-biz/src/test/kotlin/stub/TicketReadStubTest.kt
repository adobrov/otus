package ru.otus.otuskotlin.track.biz.stub

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.track.biz.TrackTicketProcessor
import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.models.*
import ru.otus.otuskotlin.track.common.stubs.TrackStubs
import ru.otus.otuskotlin.track.stubs.TrackTicketStub
import kotlin.test.Test
import kotlin.test.assertEquals

class TicketReadStubTest {

    private val processor = TrackTicketProcessor()
    val id = TrackTicketId(666)

    @Test
    fun read() = runTest {

        val ctx = TrackContext(
            command = TrackCommand.READ,
            operationState = TrackOperationState.NONE,
            workMode = TrackWorkMode.STUB,
            stubCase = TrackStubs.SUCCESS,
            ticketRequest = TrackTicket(
                id = id,
            ),
        )
        processor.exec(ctx)
        with (TrackTicketStub.get()) {
            assertEquals(id, ctx.ticketResponse.id)
            assertEquals(subject, ctx.ticketResponse.subject)
            assertEquals(description, ctx.ticketResponse.description)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = TrackContext(
            command = TrackCommand.READ,
            operationState = TrackOperationState.NONE,
            workMode = TrackWorkMode.STUB,
            stubCase = TrackStubs.BAD_ID,
            ticketRequest = TrackTicket(),
        )
        processor.exec(ctx)
        assertEquals(TrackTicket(), ctx.ticketResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = TrackContext(
            command = TrackCommand.READ,
            operationState = TrackOperationState.NONE,
            workMode = TrackWorkMode.STUB,
            stubCase = TrackStubs.DB_ERROR,
            ticketRequest = TrackTicket(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(TrackTicket(), ctx.ticketResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = TrackContext(
            command = TrackCommand.READ,
            operationState = TrackOperationState.NONE,
            workMode = TrackWorkMode.STUB,
            stubCase = TrackStubs.BAD_TITLE,
            ticketRequest = TrackTicket(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(TrackTicket(), ctx.ticketResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}