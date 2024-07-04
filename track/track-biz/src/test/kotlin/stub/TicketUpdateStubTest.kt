package ru.otus.otuskotlin.track.biz.stub

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.track.biz.TrackTicketProcessor
import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.models.*
import ru.otus.otuskotlin.track.common.stubs.TrackStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class TicketUpdateStubTest {

    private val processor = TrackTicketProcessor()
    val id = TrackTicketId(666)
    val subject = "title 666"
    val description = "desc 666"


    @Test
    fun create() = runTest {

        val ctx = TrackContext(
            command = TrackCommand.UPDATE,
            operationState = TrackOperationState.NONE,
            workMode = TrackWorkMode.STUB,
            stubCase = TrackStubs.SUCCESS,
            ticketRequest = TrackTicket(
                id = id,
                subject = subject,
                description = description,
            ),
        )
        processor.exec(ctx)
        assertEquals(id, ctx.ticketResponse.id)
        assertEquals(subject, ctx.ticketResponse.subject)
        assertEquals(description, ctx.ticketResponse.description)
    }

    @Test
    fun badId() = runTest {
        val ctx = TrackContext(
            command = TrackCommand.UPDATE,
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
    fun badTitle() = runTest {
        val ctx = TrackContext(
            command = TrackCommand.UPDATE,
            operationState = TrackOperationState.NONE,
            workMode = TrackWorkMode.STUB,
            stubCase = TrackStubs.BAD_TITLE,
            ticketRequest = TrackTicket(
                id = id,
                subject = "",
                description = description,
            ),
        )
        processor.exec(ctx)
        assertEquals(TrackTicket(), ctx.ticketResponse)
        assertEquals("title", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
    @Test
    fun badDescription() = runTest {
        val ctx = TrackContext(
            command = TrackCommand.UPDATE,
            operationState = TrackOperationState.NONE,
            workMode = TrackWorkMode.STUB,
            stubCase = TrackStubs.BAD_DESCRIPTION,
            ticketRequest = TrackTicket(
                id = id,
                subject = subject,
                description = "",
            ),
        )
        processor.exec(ctx)
        assertEquals(TrackTicket(), ctx.ticketResponse)
        assertEquals("description", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = TrackContext(
            command = TrackCommand.UPDATE,
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
            command = TrackCommand.UPDATE,
            operationState = TrackOperationState.NONE,
            workMode = TrackWorkMode.STUB,
            stubCase = TrackStubs.BAD_SEARCH_STRING,
            ticketRequest = TrackTicket(
                id = id,
                subject = subject,
                description = description,
            ),
        )
        processor.exec(ctx)
        assertEquals(TrackTicket(), ctx.ticketResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}