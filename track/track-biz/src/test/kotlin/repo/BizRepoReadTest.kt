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

class BizRepoReadTest {

    private val userId = TrackOwnerId("321")
    private val command = TrackCommand.READ
    private val initTicket = TrackTicket(
        id = TrackTicketId(123),
        subject = "abc",
        description = "abc",
        owner = userId,
    )
    private val repo = TicketRepositoryMock(
        invokeReadTicket = {
            DbTicketResponseOk(
                data = initTicket,
            )
        }
    )
    private val settings = TrackCorSettings(repoTest = repo)
    private val processor = TrackTicketProcessor(settings)

    @Test
    fun repoReadSuccessTest() = runTest {
        val ctx = TrackContext(
            command = command,
            operationState = TrackOperationState.NONE,
            workMode = TrackWorkMode.TEST,
            ticketRequest = TrackTicket(
                id = TrackTicketId(123),
            ),
        )
        processor.exec(ctx)
        assertEquals(TrackOperationState.FINISHING, ctx.operationState)
        assertEquals(initTicket.id, ctx.ticketResponse.id)
        assertEquals(initTicket.subject, ctx.ticketResponse.subject)
        assertEquals(initTicket.description, ctx.ticketResponse.description)
    }

    @Test
    fun repoReadNotFoundTest() = repoNotFoundTest(command)
}