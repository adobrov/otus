package ru.otus.otuskotlin.track.common

import ru.otus.otuskotlin.track.common.repo.IRepoTicket
import ru.otus.otuskotlin.track.logging.common.TrackLoggerProvider
import ru.otus.otuskotlin.track.common.ws.ITrackWsSessionRepo

data class TrackCorSettings(
    val loggerProvider: TrackLoggerProvider = TrackLoggerProvider(),
    val wsSessions: ITrackWsSessionRepo = ITrackWsSessionRepo.NONE,
    val repoStub: IRepoTicket = IRepoTicket.NONE,
    val repoTest: IRepoTicket = IRepoTicket.NONE,
    val repoProd: IRepoTicket = IRepoTicket.NONE,
) {
    companion object {
        val NONE = TrackCorSettings()
    }
}