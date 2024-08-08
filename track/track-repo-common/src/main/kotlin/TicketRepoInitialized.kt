package ru.otus.otuskotlin.track.repo.common

import ru.otus.otuskotlin.track.common.models.TrackTicket
class TicketRepoInitialized(
    val repo: IRepoTicketInitializable,
    initObjects: Collection<TrackTicket> = emptyList(),
) : IRepoTicketInitializable by repo {
    @Suppress("unused")
    val initializedObjects: List<TrackTicket> = save(initObjects).toList()
}