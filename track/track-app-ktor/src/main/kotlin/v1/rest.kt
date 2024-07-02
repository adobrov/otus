package ru.otus.otuskotlin.track.app.ktor.v1

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.otus.otuskotlin.track.app.ktor.TrackAppSettings

fun Route.v1Ticket(appSettings: TrackAppSettings) {
    route("ticket") {
        post("create") {
            call.createTicket(appSettings)
        }
        post("read") {
            call.readTicket(appSettings)
        }
        post("update") {
            call.updateTicket(appSettings)
        }
        post("delete") {
            call.deleteTicket(appSettings)
        }
        post("search") {
            call.searchTicket(appSettings)
        }
    }
}
