package ru.otus.otuskotlin.track.mappers.v1

import kotlinx.datetime.*
//import kotlinx.datetime.format.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.track.api.v1.models.*
import ru.otus.otuskotlin.track.common.NONE
import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.models.*
import ru.otus.otuskotlin.track.common.stubs.TrackStubs
import ru.otus.otuskotlin.track.mappers.v1.exceptions.UnknownRequestClass

private fun Int?.toTicketId() = this?.let { TrackTicketId(it) } ?: TrackTicketId.NONE
private fun Int?.toTicketWithId() = TrackTicket(id = this.toTicketId())
private fun Int?.toTicketLock() = this?.let { TrackTicketLock(it) } ?: TrackTicketLock.NONE
private fun String?.toOwnerId() = this?.let { TrackOwnerId(it) } ?: TrackOwnerId.NONE
private fun String?.toInstant() = this?.let { LocalDateTime.parse(it).toInstant(TimeZone.UTC) } ?: Instant.NONE
private fun String?.toStringSafe() = this ?: ""
private fun List<Comment>.toComments(ticketId: TrackTicketId) = this.let {
    val res = mutableListOf<TrackTicketComment>()
    for (c in this) {
        val id = c.ID ?: 0
        val author = c.author ?: ""
        var cd = c.creationDate.toInstant()
        if (c.creationDate == null) {
            cd = getCurrentTime()
        }
        val txt = c.text ?: ""
        res.addLast(
            TrackTicketComment(id, ticketId, author, cd, txt)
        )
    }
    res
}

fun TrackContext.fromTransport(request: IRequest) = when (request) {
    is TicketCreateRequest -> fromTransport(request)
    is TicketReadRequest -> fromTransport(request)
    is TicketUpdateRequest -> fromTransport(request)
    is TicketDeleteRequest -> fromTransport(request)
    is TicketSearchRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun getCurrentTime(): Instant {
    val currentMoment: Instant = Clock.System.now()
    val datetimeInUtc: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.UTC)
    return datetimeInUtc.toInstant(TimeZone.UTC)
}

private fun TicketDebug?.transportToWorkMode(): TrackWorkMode = when (this?.mode) {
    TicketRequestDebugMode.PROD -> TrackWorkMode.PROD
    TicketRequestDebugMode.TEST -> TrackWorkMode.TEST
    TicketRequestDebugMode.STUB -> TrackWorkMode.STUB
    null -> TrackWorkMode.PROD
}

private fun TicketDebug?.transportToStubCase(): TrackStubs = when (this?.stub) {
    TicketRequestDebugStubs.SUCCESS -> TrackStubs.SUCCESS
    TicketRequestDebugStubs.NOT_FOUND -> TrackStubs.NOT_FOUND
    TicketRequestDebugStubs.BAD_ID -> TrackStubs.BAD_ID
    TicketRequestDebugStubs.BAD_TITLE -> TrackStubs.BAD_TITLE
    TicketRequestDebugStubs.BAD_DESCRIPTION -> TrackStubs.BAD_DESCRIPTION
    TicketRequestDebugStubs.CANNOT_DELETE -> TrackStubs.CANNOT_DELETE
    TicketRequestDebugStubs.BAD_SEARCH_STRING -> TrackStubs.BAD_SEARCH_STRING
    null -> TrackStubs.NONE
}

private fun State?.toState(): TrackState = when (this?.name) {
    State.NEW.name -> TrackState.NEW
    State.PROGRESS.name -> TrackState.PROGRESS
    State.FINISH.name -> TrackState.FINISH
    null -> TrackState.NONE
    else -> TrackState.NONE
}

fun TrackContext.fromTransport(request: TicketCreateRequest) {
    command = TrackCommand.CREATE
    ticketRequest = request.ticket?.toInternal() ?: TrackTicket()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun TrackContext.fromTransport(request: TicketReadRequest) {
    command = TrackCommand.READ
    ticketRequest = request.ticket.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun TicketReadObject?.toInternal(): TrackTicket = if (this != null) {
    TrackTicket(id = id.toTicketId())
} else {
    TrackTicket()
}

fun TrackContext.fromTransport(request: TicketUpdateRequest) {
    command = TrackCommand.UPDATE
    ticketRequest = request.ticket?.toInternal() ?: TrackTicket()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun TrackContext.fromTransport(request: TicketDeleteRequest) {
    command = TrackCommand.DELETE
    ticketRequest = request.ticket.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun TicketDeleteObject?.toInternal(): TrackTicket = if (this != null) {
    TrackTicket(
        id = id.toTicketId(),
        lock = lock.toTicketLock(),
    )
} else {
    TrackTicket()
}

fun TrackContext.fromTransport(request: TicketSearchRequest) {
    command = TrackCommand.SEARCH
    ticketFilterRequest = request.filter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun TrackContext.fromTransport(request: TicketAddCommentRequest) {
    command = TrackCommand.ADD

    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()

    newComment = TrackTicketComment(
        ticketId = request.comment?.id.toTicketId(),
        author = request.comment?.author.toStringSafe(),
        creationDate = getCurrentTime(),
        text = request.comment?.text.toStringSafe(),
    )
}
private fun TicketSearchFilterObject?.toInternal(): TrackTicketFilter = TrackTicketFilter(
    searchString = this?.searchString ?: ""
)

private fun TicketCreateObject.toInternal(): TrackTicket = TrackTicket(
    subject = this.subject ?: "",
    description = this.description ?: "",
    owner = this.owner.toOwnerId(),
    comments = this.comment?.let {it.toComments(this.id.toTicketId())} ?: mutableListOf(),
    creationDate = getCurrentTime(),
)

private fun TicketUpdateObject.toInternal(): TrackTicket = TrackTicket(
    id = this.id.toTicketId(),
    description = this.description ?: "",
    owner = this.owner.toOwnerId(),
    state = this.state.toState(),
    finishDate = this.finishDate.toInstant(), //TODO: DateTimeFormatException
    lock = lock.toTicketLock(),
)

