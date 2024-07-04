package ru.otus.otuskotlin.track.biz.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.models.TrackTicketFilter
import ru.otus.otuskotlin.track.common.models.TrackOperationState
import ru.otus.otuskotlin.track.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateSearchStringLengthTest {
    @Test
    fun emptyString() = runTest {
        val ctx = TrackContext(
            operationState = TrackOperationState.RUNNING,
            ticketFilterValidating = TrackTicketFilter(searchString = "")
        )
        chain.exec(ctx)
        assertEquals(TrackOperationState.RUNNING, ctx.operationState)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun blankString() = runTest {
        val ctx = TrackContext(
            operationState = TrackOperationState.RUNNING,
            ticketFilterValidating = TrackTicketFilter(searchString = "  ")
        )
        chain.exec(ctx)
        assertEquals(TrackOperationState.RUNNING, ctx.operationState)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun shortString() = runTest {
        val ctx = TrackContext(
            operationState = TrackOperationState.RUNNING,
            ticketFilterValidating = TrackTicketFilter(searchString = "12")
        )
        chain.exec(ctx)
        assertEquals(TrackOperationState.FAILING, ctx.operationState)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooShort", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = TrackContext(
            operationState = TrackOperationState.RUNNING,
            ticketFilterValidating = TrackTicketFilter(searchString = "123")
        )
        chain.exec(ctx)
        assertEquals(TrackOperationState.RUNNING, ctx.operationState)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun longString() = runTest {
        val ctx = TrackContext(
            operationState = TrackOperationState.RUNNING,
            ticketFilterValidating = TrackTicketFilter(searchString = "12".repeat(51))
        )
        chain.exec(ctx)
        assertEquals(TrackOperationState.FAILING, ctx.operationState)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooLong", ctx.errors.first().code)
    }

    companion object {
        val chain = rootChain {
            validateSearchStringLength("")
        }.build()
    }
}