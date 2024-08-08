package ru.otus.otuskotlin.track.backend.repo.postgresql

import ru.otus.otuskotlin.track.backend.repo.tests.*
import ru.otus.otuskotlin.track.common.repo.IRepoTicket
import ru.otus.otuskotlin.track.repo.common.TicketRepoInitialized
import ru.otus.otuskotlin.track.repo.common.IRepoTicketInitializable
import kotlin.test.AfterTest

private fun IRepoTicket.clear() {
    val pgRepo = (this as TicketRepoInitialized).repo as RepoTicketSql
    pgRepo.clear()
}

class RepoTicketSQLCreateTest : RepoTicketCreateTest() {
    override val repo: IRepoTicketInitializable = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
    )
    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoTicketSQLReadTest : RepoTicketReadTest() {
    override val repo: IRepoTicketInitializable = SqlTestCompanion.repoUnderTestContainer(initObjects)
    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoTicketSQLUpdateTest : RepoTicketUpdateTest() {
    override val repo: IRepoTicket = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        //randomUuid = { lockNew.asString() },
    )
    @AfterTest
    fun tearDown() {
        repo.clear()
    }
}

class RepoTicketSQLDeleteTest : RepoTicketDeleteTest() {
    override val repo: IRepoTicket = SqlTestCompanion.repoUnderTestContainer(initObjects)
    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoTicketSQLSearchTest : RepoTicketSearchTest() {
    override val repo: IRepoTicket = SqlTestCompanion.repoUnderTestContainer(initObjects)
    @AfterTest
    fun tearDown() = repo.clear()
}