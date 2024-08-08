package ru.otus.otuskotlin.track.backend.repo.tests

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.track.common.models.TrackTicket
import ru.otus.otuskotlin.track.common.repo.*
import ru.otus.otuskotlin.track.stubs.TrackTicketStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class TicketRepositoryMockTest {
    private val repo = TicketRepositoryMock(
        invokeCreateTicket = { DbTicketResponseOk(TrackTicketStub.prepareResult { subject = "create" }) },
        invokeReadTicket = { DbTicketResponseOk(TrackTicketStub.prepareResult { subject = "read" }) },
        invokeUpdateTicket = { DbTicketResponseOk(TrackTicketStub.prepareResult { subject = "update" }) },
        invokeDeleteTicket = { DbTicketResponseOk(TrackTicketStub.prepareResult { subject = "delete" }) },
        invokeSearchTicket = { DbTicketsResponseOk(listOf(TrackTicketStub.prepareResult { subject = "search" })) },
    )

    @Test
    fun mockCreate() = runTest {
        val result = repo.createTicket(DbTicketRequest(TrackTicket()))
        assertIs<DbTicketResponseOk>(result)
        assertEquals("create", result.data.subject)
    }

    @Test
    fun mockRead() = runTest {
        val result = repo.readTicket(DbTicketIdRequest(TrackTicket()))
        assertIs<DbTicketResponseOk>(result)
        assertEquals("read", result.data.subject)
    }

    @Test
    fun mockUpdate() = runTest {
        val result = repo.updateTicket(DbTicketRequest(TrackTicket()))
        assertIs<DbTicketResponseOk>(result)
        assertEquals("update", result.data.subject)
    }

    @Test
    fun mockDelete() = runTest {
        val result = repo.deleteTicket(DbTicketIdRequest(TrackTicket()))
        assertIs<DbTicketResponseOk>(result)
        assertEquals("delete", result.data.subject)
    }

    @Test
    fun mockSearch() = runTest {
        val result = repo.searchTicket(DbTicketFilterRequest())
        assertIs<DbTicketsResponseOk>(result)
        assertEquals("search", result.data.first().subject)
    }

}