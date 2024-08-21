package ru.otus.otuskotlin.track.backend.repo.tests

import ru.otus.otuskotlin.track.common.models.TrackTicket
import ru.otus.otuskotlin.track.common.models.TrackOwnerId
import ru.otus.otuskotlin.track.common.repo.DbTicketFilterRequest
import ru.otus.otuskotlin.track.common.repo.DbTicketsResponseOk
import ru.otus.otuskotlin.track.common.repo.IRepoTicket
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoTicketSearchTest {
    abstract val repo: IRepoTicket

    protected open val initializedObjects: List<TrackTicket> = initObjects

    @Test
    fun searchOwner() = runRepoTest {
        val result = repo.searchTicket(DbTicketFilterRequest(ownerId = searchOwnerId))
        assertIs<DbTicketsResponseOk>(result)
        val expected = listOf(initializedObjects[2]).sortedBy { it.id.asInt() }
        assertEquals(expected, result.data.sortedBy { it.id.asInt() })
    }


    companion object: BaseInitTickets(3) {

        val searchOwnerId = TrackOwnerId("owner-124")
        override val initObjects: List<TrackTicket> = listOf(
            createInitTestModel("ad1"),
            createInitTestModel("ad2"),
            createInitTestModel("ad4", ownerId = searchOwnerId),
        )
    }
}