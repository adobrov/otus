package ru.otus.otuskotlin.track.mappers.v1

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import ru.otus.otuskotlin.track.api.v1.models.*
import ru.otus.otuskotlin.track.common.NONE
import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.exceptions.UnknownTrackCommand
import ru.otus.otuskotlin.track.common.models.*


fun TrackContext.toTransportTicket(): IResponse = when (val cmd = command) {
    TrackCommand.CREATE -> toTransportCreate()
    TrackCommand.READ -> toTransportRead()
    TrackCommand.UPDATE -> toTransportUpdate()
    TrackCommand.DELETE -> toTransportDelete()
    TrackCommand.SEARCH -> toTransportSearch()
    else -> throw UnknownTrackCommand(cmd)
}

fun TrackContext.toTransportCreate() = TicketCreateResponse(
    result = operationState.toResult(),
    errors = errors.toTransportErrors(),
    ticket = ticketResponse.toTransportTicket(),
)

fun TrackContext.toTransportRead() = TicketReadResponse(
    result = operationState.toResult(),
    errors = errors.toTransportErrors(),
    ticket = ticketResponse.toTransportTicket(),
)

fun TrackContext.toTransportUpdate() = TicketUpdateResponse(
    result = operationState.toResult(),
    errors = errors.toTransportErrors(),
    ticket = ticketResponse.toTransportTicket()
)

fun TrackContext.toTransportDelete() = TicketDeleteResponse(
    result = operationState.toResult(),
    errors = errors.toTransportErrors(),
    ticket = ticketResponse.toTransportTicket()
)

fun TrackContext.toTransportSearch() = TicketSearchResponse(
    result = operationState.toResult(),
    errors = errors.toTransportErrors(),
    tickets = ticketsResponse.toTransportTicket()
)

fun TrackContext.toTransportAdd() = TicketAddCommentResponse(
    result = operationState.toResult(),
    errors = errors.toTransportErrors(),
)

fun TrackContext.toTransportInit() = TicketInitResponse(
    result = operationState.toResult(),
    errors = errors.toTransportErrors(),
)

private fun TrackState.toResult(): State? = when (this) {
    TrackState.NEW -> State.NEW
    TrackState.PROGRESS -> State.PROGRESS
    TrackState.FINISH -> State.FINISH
    TrackState.NONE -> null
}
private fun TrackTicket.toTransportTicket(): TicketResponseObject = TicketResponseObject(
    id = id.takeIf { it != TrackTicketId.NONE }?.asInt(),
    subject = subject.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    owner = owner.takeIf { it != TrackOwnerId.NONE }?.asString(),
    state = state.toResult(),
    finishDate = finishDate.takeIf { it != Instant.NONE }?.toString(),
    creationDate = creationDate.takeIf { it != Instant.NONE }?.toString(),
    comment = comments.toTransportTicketComment(id),
)

private fun TrackTicketComment.toTransportTicket(tid: TrackTicketId): Comment = Comment(
    ID = id,
    ticketId = tid.takeIf { it != TrackTicketId.NONE }?.asInt(),
    author = author,
    creationDate = creationDate.takeIf { it != Instant.NONE }?.toString(),
    text = text
)

private fun List<TrackTicketComment>.toTransportTicketComment(id: TrackTicketId): List<Comment>? = this
    .map { it.toTransportTicket(id) }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun List<TrackTicket>.toTransportTicket(): List<TicketResponseObject>? = this
    .map { it.toTransportTicket() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun List<TrackError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportTicket() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun TrackError.toTransportTicket() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

private fun TrackOperationState.toResult(): ResponseResult? = when (this) {
    TrackOperationState.RUNNING -> ResponseResult.SUCCESS
    TrackOperationState.FAILING -> ResponseResult.ERROR
    TrackOperationState.FINISHING -> ResponseResult.SUCCESS
    TrackOperationState.NONE -> null
}