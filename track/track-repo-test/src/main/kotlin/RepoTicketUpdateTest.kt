package ru.otus.otuskotlin.track.backend.repo.tests

import ru.otus.otuskotlin.track.common.models.*
import ru.otus.otuskotlin.track.common.repo.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoTicketUpdateTest {
    abstract val repo: IRepoTicket
    protected open val updateSucc = initObjects[0]
    protected val updateIdNotFound = TrackOwnerId("ticket-repo-update-not-found")

    private val reqUpdateSucc by lazy {
        TrackTicket(
            id = updateSucc.id,
            subject = "update object",
            description = "update object description",
            owner = TrackOwnerId("owner-123"),
            state = TrackState.NEW,
        )
    }
    private val reqUpdateNotFound = TrackTicket(
        id = TrackTicketId(666),
        subject = "update object not found",
        description = "update object not found description",
        owner = TrackOwnerId("owner-123"),
        state = TrackState.NEW,
    )

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateTicket(DbTicketRequest(reqUpdateSucc))
        assertIs<DbTicketResponseOk>(result)
        assertEquals(reqUpdateSucc.id, result.data.id)
        assertEquals(reqUpdateSucc.subject, result.data.subject)
        assertEquals(reqUpdateSucc.description, result.data.description)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateTicket(DbTicketRequest(reqUpdateNotFound))
        assertIs<DbTicketResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitTickets(2) {
        override val initObjects: List<TrackTicket> = listOf(
            createInitTestModel("update"),
        )
    }
}