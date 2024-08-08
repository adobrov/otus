package ru.otus.otuskotlin.track.biz.repo

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.track.backend.repo.tests.TicketRepositoryMock
import ru.otus.otuskotlin.track.biz.TrackTicketProcessor
import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.TrackCorSettings
import ru.otus.otuskotlin.track.common.models.*
import ru.otus.otuskotlin.track.common.repo.DbTicketResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizRepoCreateTest {
    private val userId = TrackOwnerId("321")
    private val command = TrackCommand.CREATE
    private val uuid = 1111
    private val repo = TicketRepositoryMock(
        invokeCreateTicket = {
            DbTicketResponseOk(
                data = TrackTicket(
                    id = TrackTicketId(uuid),
                    subject = it.ticket.subject,
                    description = it.ticket.description,
                    owner = userId,
                )
            )
        }
    )
    private val settings = TrackCorSettings(
        repoTest = repo
    )
    private val processor = TrackTicketProcessor(settings)

    @Test
    fun repoCreateSuccessTest() = runTest {
        val ctx = TrackContext(
            command = command,
            operationState = TrackOperationState.NONE,
            workMode = TrackWorkMode.TEST,
            ticketRequest = TrackTicket(
                subject = "abc",
                description = "abc"
            ),
        )
        processor.exec(ctx)
        assertEquals(TrackOperationState.FINISHING, ctx.operationState)
        assertNotEquals(TrackTicketId.NONE, ctx.ticketResponse.id)
        assertEquals("abc", ctx.ticketResponse.subject)
        assertEquals("abc", ctx.ticketResponse.description)
    }
}