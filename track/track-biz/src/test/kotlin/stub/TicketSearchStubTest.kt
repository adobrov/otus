package ru.otus.otuskotlin.track.biz.stub

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.track.biz.TrackTicketProcessor
import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.models.*
import ru.otus.otuskotlin.track.common.stubs.TrackStubs
import ru.otus.otuskotlin.track.stubs.TrackTicketStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class TicketSearchStubTest {

    private val processor = TrackTicketProcessor()
    val filter = TrackTicketFilter(searchString = "Заявка")

    @Test
    fun read() = runTest {

        val ctx = TrackContext(
            command = TrackCommand.SEARCH,
            operationState = TrackOperationState.NONE,
            workMode = TrackWorkMode.STUB,
            stubCase = TrackStubs.SUCCESS,
            ticketFilterRequest = filter,
        )
        processor.exec(ctx)
        assertTrue(ctx.ticketsResponse.size > 1)
        val first = ctx.ticketsResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.subject.contains(filter.searchString))
        assertTrue(first.description.contains(filter.searchString))
    }

    @Test
    fun badId() = runTest {
        val ctx = TrackContext(
            command = TrackCommand.SEARCH,
            operationState = TrackOperationState.NONE,
            workMode = TrackWorkMode.STUB,
            stubCase = TrackStubs.BAD_ID,
            ticketFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(TrackTicket(), ctx.ticketResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = TrackContext(
            command = TrackCommand.SEARCH,
            operationState = TrackOperationState.NONE,
            workMode = TrackWorkMode.STUB,
            stubCase = TrackStubs.DB_ERROR,
            ticketFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(TrackTicket(), ctx.ticketResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = TrackContext(
            command = TrackCommand.SEARCH,
            operationState = TrackOperationState.NONE,
            workMode = TrackWorkMode.STUB,
            stubCase = TrackStubs.BAD_TITLE,
            ticketFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(TrackTicket(), ctx.ticketResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}