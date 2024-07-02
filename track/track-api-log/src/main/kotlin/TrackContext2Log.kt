package ru.otus.otuskotlin.track.api.log.mapper

import kotlinx.datetime.Clock
import ru.otus.otuskotlin.track.api.log.models.*
import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.models.*

fun TrackContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "track",
    ticket = toTrackLog(),
    errors = errors.map { it.toLog() },
)

private fun TrackContext.toTrackLog(): TicketLogModel? {
    val ticketNone = TrackTicket()
    return TicketLogModel(
        requestId = requestId.takeIf { it != TrackRequestId.NONE }?.asString(),
        requestTicket = ticketRequest.takeIf { it != ticketNone }?.toLog(),
        responseTicket = ticketResponse.takeIf { it != ticketNone }?.toLog(),
        responseTickets = ticketsResponse.takeIf { it.isNotEmpty() }?.filter { it != ticketNone }?.map { it.toLog() },
        requestFilter = ticketFilterRequest.takeIf { it != TrackTicketFilter() }?.toLog(),
    ).takeIf { it != TicketLogModel() }
}

private fun TrackTicketFilter.toLog() = TicketFilterLog(
    searchString = searchString.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != TrackOwnerId.NONE }?.asString(),
    state = state.takeIf { it != TrackState.NONE }?.name,
)

private fun TrackError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

private fun TrackTicket.toLog() = TicketLog(
    id = id.takeIf { it != TrackTicketId.NONE }?.asInt()?.toString() ?: "",
//    title = title.takeIf { it.isNotBlank() },
//    description = description.takeIf { it.isNotBlank() },
//    adType = adType.takeIf { it != MkplDealSide.NONE }?.name,
//    visibility = visibility.takeIf { it != MkplVisibility.NONE }?.name,
//    ownerId = ownerId.takeIf { it != TrackOwnerId.NONE }?.asString(),
//    productId = productId.takeIf { it != MkplProductId.NONE }?.asString(),
//    permissions = permissionsClient.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet(),
)