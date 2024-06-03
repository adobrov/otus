package ru.otus.otuskotlin.track.common

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.track.common.models.*
import ru.otus.otuskotlin.track.common.stubs.TrackStubs

data class TrackContext(
    var command: TrackCommand = TrackCommand.NONE,
    val errors: MutableList<TrackError> = mutableListOf(),

    var workMode: TrackWorkMode = TrackWorkMode.PROD,
    var stubCase: TrackStubs = TrackStubs.NONE,

    var requestId: TrackRequestId = TrackRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var ticketRequest: TrackTicket = TrackTicket(),
    var ticketFilterRequest: TrackTicketFilter = TrackTicketFilter(),

    var ticketResponse: TrackTicket = TrackTicket(),
    var ticketsResponse: MutableList<TrackTicket> = mutableListOf(),

    var newComment: TrackTicketComment = TrackTicketComment.NONE,
    var operationState: TrackOperationState = TrackOperationState.NONE,
)
