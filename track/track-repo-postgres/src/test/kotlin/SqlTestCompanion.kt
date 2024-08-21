package ru.otus.otuskotlin.track.backend.repo.postgresql

import ru.otus.otuskotlin.track.common.models.TrackTicket
import ru.otus.otuskotlin.track.repo.common.TicketRepoInitialized
import ru.otus.otuskotlin.track.repo.common.IRepoTicketInitializable

object SqlTestCompanion {
    private const val HOST = "localhost"
    private const val USER = "trackUser"
    private const val PASS = "123456"
    val PORT = getEnv("postgresPort")?.toIntOrNull() ?: 5432

    fun repoUnderTestContainer(
        initObjects: Collection<TrackTicket> = emptyList(),
    ): IRepoTicketInitializable = TicketRepoInitialized(
        repo = RepoTicketSql(
            SqlProperties(
                host = HOST,
                user = USER,
                password = PASS,
                port = PORT,
            ),
        ),
        initObjects = initObjects,
    )
}