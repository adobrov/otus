package ru.otus.otuskotlin.track.biz.repo

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.track.backend.repo.tests.TicketRepositoryMock
import ru.otus.otuskotlin.track.biz.TrackTicketProcessor
import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.TrackCorSettings
import ru.otus.otuskotlin.track.common.models.*
import ru.otus.otuskotlin.track.common.repo.DbTicketsResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoSearchTest {

    private val userId = TrackOwnerId("321")
    private val command = TrackCommand.SEARCH
    private val initTicket = TrackTicket(
        id = TrackTicketId(123),
        subject = "abc",
        description = "abc",
        owner = userId,
    )
    private val repo = TicketRepositoryMock(
        invokeSearchTicket = {
            DbTicketsResponseOk(
                data = listOf(initTicket),
            )
        }
    )
    private val settings = TrackCorSettings(repoTest = repo)
    private val processor = TrackTicketProcessor(settings)

    @Test
    fun repoSearchSuccessTest() = runTest {
        val ctx = TrackContext(
            command = command,
            operationState = TrackOperationState.NONE,
            workMode = TrackWorkMode.TEST,
            ticketFilterRequest = TrackTicketFilter(
                searchString = "abc",
            ),
        )
        processor.exec(ctx)
        assertEquals(TrackOperationState.FINISHING, ctx.operationState)
        assertEquals(1, ctx.ticketsResponse.size)
    }
}