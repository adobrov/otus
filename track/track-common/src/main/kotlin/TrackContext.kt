package ru.otus.otuskotlin.track.common

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.track.common.models.*
import ru.otus.otuskotlin.track.common.repo.IRepoTicket
import ru.otus.otuskotlin.track.common.stubs.TrackStubs
import ru.otus.otuskotlin.track.common.ws.ITrackWsSession

data class TrackContext(
    var command: TrackCommand = TrackCommand.NONE,
    val errors: MutableList<TrackError> = mutableListOf(),

    var corSettings: TrackCorSettings = TrackCorSettings(),
    var workMode: TrackWorkMode = TrackWorkMode.PROD,
    var stubCase: TrackStubs = TrackStubs.NONE,

    var wsSession: ITrackWsSession = ITrackWsSession.NONE,

    var requestId: TrackRequestId = TrackRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var ticketRequest: TrackTicket = TrackTicket(),
    var ticketFilterRequest: TrackTicketFilter = TrackTicketFilter(),

    var ticketResponse: TrackTicket = TrackTicket(),
    var ticketsResponse: MutableList<TrackTicket> = mutableListOf(),


    var ticketValidating: TrackTicket = TrackTicket(),
    var ticketFilterValidating: TrackTicketFilter = TrackTicketFilter(),

    var ticketValidated: TrackTicket = TrackTicket(),
    var ticketFilterValidated: TrackTicketFilter = TrackTicketFilter(),

    var ticketRepo: IRepoTicket = IRepoTicket.NONE,
    var ticketRepoRead: TrackTicket = TrackTicket(), // То, что прочитали из репозитория
    var ticketRepoPrepare: TrackTicket = TrackTicket(), // То, что готовим для сохранения в БД
    var ticketRepoDone: TrackTicket = TrackTicket(),  // Результат, полученный из БД
    var ticketsRepoDone: MutableList<TrackTicket> = mutableListOf(),

    var newComment: TrackTicketComment = TrackTicketComment.NONE,
    var operationState: TrackOperationState = TrackOperationState.NONE,
)
