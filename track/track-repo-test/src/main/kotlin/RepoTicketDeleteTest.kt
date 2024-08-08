package ru.otus.otuskotlin.track.backend.repo.tests

import ru.otus.otuskotlin.track.common.models.TrackTicket
import ru.otus.otuskotlin.track.common.models.TrackTicketId
import ru.otus.otuskotlin.track.common.repo.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

abstract class RepoTicketDeleteTest {
    abstract val repo: IRepoTicket
    protected open val deleteSucc = initObjects[0]
    protected open val notFoundId = TrackTicketId(222)

    @Test
    fun deleteSuccess() = runRepoTest {
        val result = repo.deleteTicket(DbTicketIdRequest(deleteSucc.id))
        assertIs<DbTicketResponseOk>(result)
        assertEquals(deleteSucc.subject, result.data.subject)
        assertEquals(deleteSucc.description, result.data.description)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readTicket(DbTicketIdRequest(notFoundId))

        assertIs<DbTicketResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertNotNull(error)
    }

    companion object : BaseInitTickets(1) {
        override val initObjects: List<TrackTicket> = listOf(
            createInitTestModel("delete"),
        )
    }
}