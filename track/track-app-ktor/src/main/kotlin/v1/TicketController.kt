package ru.otus.otuskotlin.track.app.ktor.v1

import io.ktor.server.application.*
import ru.otus.otuskotlin.track.api.v1.models.*
import ru.otus.otuskotlin.track.app.ktor.TrackAppSettings
import kotlin.reflect.KClass

val clCreate: KClass<*> = ApplicationCall::createTicket::class
suspend fun ApplicationCall.createTicket(appSettings: TrackAppSettings) =
    processV1<TicketCreateRequest, TicketCreateResponse>(appSettings, clCreate,"create")

val clRead: KClass<*> = ApplicationCall::readTicket::class
suspend fun ApplicationCall.readTicket(appSettings: TrackAppSettings) =
    processV1<TicketReadRequest, TicketReadResponse>(appSettings, clRead, "read")

val clUpdate: KClass<*> = ApplicationCall::updateTicket::class
suspend fun ApplicationCall.updateTicket(appSettings: TrackAppSettings) =
    processV1<TicketUpdateRequest, TicketUpdateResponse>(appSettings, clUpdate, "update")

val clDelete: KClass<*> = ApplicationCall::deleteTicket::class
suspend fun ApplicationCall.deleteTicket(appSettings: TrackAppSettings) =
    processV1<TicketDeleteRequest, TicketDeleteResponse>(appSettings, clDelete, "delete")

val clSearch: KClass<*> = ApplicationCall::searchTicket::class
suspend fun ApplicationCall.searchTicket(appSettings: TrackAppSettings) =
    processV1<TicketSearchRequest, TicketSearchResponse>(appSettings, clSearch, "search")

