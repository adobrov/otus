package ru.otus.otuskotlin.track.biz.repo

import kotlinx.coroutines.test.runTest
import repo.repoNotFoundTest
import ru.otus.otuskotlin.track.backend.repo.tests.TicketRepositoryMock
import ru.otus.otuskotlin.track.biz.TrackTicketProcessor
import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.TrackCorSettings
import ru.otus.otuskotlin.track.common.models.*
import ru.otus.otuskotlin.track.common.repo.DbTicketResponseErr
import ru.otus.otuskotlin.track.common.repo.DbTicketResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BizRepoDeleteTest {

    private val userId = TrackOwnerId("321")
    private val command = TrackCommand.DELETE
    private val initTicket = TrackTicket(
        id = TrackTicketId(123),
        subject = "abc",
        description = "abc",
        owner = userId,
        lock = TrackTicketLock(1),
    )
    private val repo = TicketRepositoryMock(
        invokeReadTicket = {
            DbTicketResponseOk(
                data = initTicket,
            )
        },
        invokeDeleteTicket = {
            if (it.id == initTicket.id)
                DbTicketResponseOk(
                    data = initTicket
                )
            else DbTicketResponseErr()
        }
    )
    private val settings by lazy {
        TrackCorSettings(
            repoTest = repo
        )
    }
    private val processor = TrackTicketProcessor(settings)

    @Test
    fun repoDeleteSuccessTest() = runTest {
        val ticketToDelete = TrackTicket(
            id = TrackTicketId(123),
            lock = TrackTicketLock(1),
        )
        val ctx = TrackContext(
            command = command,
            operationState = TrackOperationState.NONE,
            workMode = TrackWorkMode.TEST,
            ticketRequest = ticketToDelete,
        )
        processor.exec(ctx)
        assertEquals(TrackOperationState.FINISHING, ctx.operationState)
        assertTrue { ctx.errors.isEmpty() }
        assertEquals(initTicket.id, ctx.ticketResponse.id)
        assertEquals(initTicket.subject, ctx.ticketResponse.subject)
        assertEquals(initTicket.description, ctx.ticketResponse.description)
    }

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(command)
}