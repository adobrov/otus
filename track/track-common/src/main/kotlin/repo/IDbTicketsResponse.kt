package ru.otus.otuskotlin.track.common.repo

import ru.otus.otuskotlin.track.common.models.TrackTicket
import ru.otus.otuskotlin.track.common.models.TrackError

sealed interface IDbTicketsResponse: IDbResponse<List<TrackTicket>>

data class DbTicketsResponseOk(
    val data: List<TrackTicket>
): IDbTicketsResponse

@Suppress("unused")
data class DbTicketsResponseErr(
    val errors: List<TrackError> = emptyList()
): IDbTicketsResponse {
    constructor(err: TrackError): this(listOf(err))
}