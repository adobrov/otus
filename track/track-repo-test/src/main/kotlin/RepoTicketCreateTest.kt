package ru.otus.otuskotlin.track.backend.repo.tests

import ru.otus.otuskotlin.track.common.models.*
import ru.otus.otuskotlin.track.common.repo.DbTicketRequest
import ru.otus.otuskotlin.track.common.repo.DbTicketResponseOk
import ru.otus.otuskotlin.track.repo.common.IRepoTicketInitializable
import kotlin.test.*


abstract class RepoTicketCreateTest {
    abstract val repo: IRepoTicketInitializable
    protected open val uuidNew = TrackTicketId(1)

    private val createObj = TrackTicket(
        id = uuidNew,
        subject = "create object",
        description = "create object description",
        owner = TrackOwnerId("owner-123"),
        state = TrackState.NEW,
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createTicket(DbTicketRequest(createObj))
        val expected = createObj
        assertIs<DbTicketResponseOk>(result)
        assertEquals(uuidNew, result.data.id)
        assertEquals(expected.subject, result.data.subject)
        assertEquals(expected.description, result.data.description)
        assertNotEquals(TrackTicketId.NONE, result.data.id)
    }

    companion object : BaseInitTickets(1) {
        override val initObjects: List<TrackTicket> = emptyList()
    }
}