package ru.otus.otuskotlin.track.biz.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.models.TrackTicket
import ru.otus.otuskotlin.track.common.models.TrackTicketFilter
import ru.otus.otuskotlin.track.common.models.TrackOperationState
import ru.otus.otuskotlin.track.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateTitleHasContentTest {
    @Test
    fun emptyString() = runTest {
        val ctx = TrackContext(operationState = TrackOperationState.RUNNING, ticketValidating = TrackTicket(subject = ""))
        chain.exec(ctx)
        assertEquals(TrackOperationState.RUNNING, ctx.operationState)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun noContent() = runTest {
        val ctx = TrackContext(operationState = TrackOperationState.RUNNING, ticketValidating = TrackTicket(subject = "12!@#$%^&*()_+-="))
        chain.exec(ctx)
        assertEquals(TrackOperationState.FAILING, ctx.operationState)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-title-noContent", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = TrackContext(operationState = TrackOperationState.RUNNING, ticketFilterValidating = TrackTicketFilter(searchString = "Ж"))
        chain.exec(ctx)
        assertEquals(TrackOperationState.RUNNING, ctx.operationState)
        assertEquals(0, ctx.errors.size)
    }

    companion object {
        val chain = rootChain {
            validateSubjectHasContent("")
        }.build()
    }
}