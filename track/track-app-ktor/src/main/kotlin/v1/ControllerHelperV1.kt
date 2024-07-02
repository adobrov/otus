package ru.otus.otuskotlin.track.app.ktor.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.otuskotlin.track.api.v1.models.IRequest
import ru.otus.otuskotlin.track.api.v1.models.IResponse
import ru.otus.otuskotlin.track.app.common.controllerHelper
import ru.otus.otuskotlin.track.app.ktor.TrackAppSettings
import ru.otus.otuskotlin.track.mappers.v1.fromTransport
import ru.otus.otuskotlin.track.mappers.v1.toTransportTicket
import kotlin.reflect.KClass

suspend inline fun <reified Q : IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processV1(
    appSettings: TrackAppSettings,
    clazz: KClass<*>,
    logId: String,
) = appSettings.controllerHelper(
    {
        fromTransport(receive<Q>())
    },
    { this@processV1.respond(toTransportTicket() as R) },
    clazz,
    logId,
)
