package ru.otus.otuskotlin.track.common.repo

import ru.otus.otuskotlin.track.common.models.TrackTicket
import ru.otus.otuskotlin.track.common.models.TrackError

sealed interface IDbTicketResponse: IDbResponse<TrackTicket>

data class DbTicketResponseOk(
    val data: TrackTicket
): IDbTicketResponse

data class DbTicketResponseErr(
    val errors: List<TrackError> = emptyList()
): IDbTicketResponse {
    constructor(err: TrackError): this(listOf(err))
}

data class DbTicketResponseErrWithData(
    val data: TrackTicket,
    val errors: List<TrackError> = emptyList()
): IDbTicketResponse {
    constructor(ticket: TrackTicket, err: TrackError): this(ticket, listOf(err))
}