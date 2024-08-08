package ru.otus.otuskotlin.track.repo.common

import ru.otus.otuskotlin.track.common.models.TrackTicket
import ru.otus.otuskotlin.track.common.repo.IRepoTicket

interface IRepoTicketInitializable: IRepoTicket {
    fun save(tickets: Collection<TrackTicket>): Collection<TrackTicket>
}