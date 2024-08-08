package ru.otus.otuskotlin.track.backend.repo.tests

import ru.otus.otuskotlin.track.common.models.TrackTicket
import ru.otus.otuskotlin.track.common.models.TrackTicketId
import ru.otus.otuskotlin.track.common.repo.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoTicketReadTest {
    abstract val repo: IRepoTicket
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readTicket(DbTicketIdRequest(readSucc.id))

        assertIs<DbTicketResponseOk>(result)
        assertEquals(readSucc, result.data)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readTicket(DbTicketIdRequest(notFoundId))

        assertIs<DbTicketResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitTickets(2) {
        override val initObjects: List<TrackTicket> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = TrackTicketId(333)

    }
}