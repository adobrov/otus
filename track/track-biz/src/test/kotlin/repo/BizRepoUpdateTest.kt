package ru.otus.otuskotlin.track.biz.repo

import kotlinx.coroutines.test.runTest
import repo.repoNotFoundTest
import ru.otus.otuskotlin.track.backend.repo.tests.TicketRepositoryMock
import ru.otus.otuskotlin.track.biz.TrackTicketProcessor
import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.TrackCorSettings
import ru.otus.otuskotlin.track.common.models.*
import ru.otus.otuskotlin.track.common.repo.DbTicketResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoUpdateTest {

    private val userId = TrackOwnerId("321")
    private val command = TrackCommand.UPDATE
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
        invokeUpdateTicket = {
            DbTicketResponseOk(
                data = TrackTicket(
                    id = TrackTicketId(123),
                    subject = "xyz",
                    description = "xyz",
                    lock = TrackTicketLock(1),
                )
            )
        }
    )
    private val settings = TrackCorSettings(repoTest = repo)
    private val processor = TrackTicketProcessor(settings)

    @Test
    fun repoUpdateSuccessTest() = runTest {
        val ticketToUpdate = TrackTicket(
            id = TrackTicketId(123),
            subject = "xyz",
            description = "xyz",
            lock = TrackTicketLock(1),
        )
        val ctx = TrackContext(
            command = command,
            operationState = TrackOperationState.NONE,
            workMode = TrackWorkMode.TEST,
            ticketRequest = ticketToUpdate,
        )
        processor.exec(ctx)
        assertEquals(TrackOperationState.FINISHING, ctx.operationState)
        assertEquals(ticketToUpdate.id, ctx.ticketResponse.id)
        assertEquals(ticketToUpdate.subject, ctx.ticketResponse.subject)
        assertEquals(ticketToUpdate.description, ctx.ticketResponse.description)
    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(command)
}