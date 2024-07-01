package ru.otus.otuskotlin.track.app.ktor

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.serialization.jackson.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.websocket.*
import org.slf4j.event.Level
import ru.otus.otuskotlin.track.api.v1.apiV1Mapper
import ru.otus.otuskotlin.track.app.ktor.v1.v1Ticket
import ru.otus.otuskotlin.track.app.ktor.plugins.initAppSettings
import ru.otus.otuskotlin.track.app.ktor.v1.wsHandlerV1
import io.ktor.server.application.*


@Suppress("unused") // Referenced in application.conf
fun Application.moduleJvm(
    appSettings: TrackAppSettings = initAppSettings(),
) {
    install(CachingHeaders)
    install(DefaultHeaders)
    install(AutoHeadResponse)
    install(CallLogging) {
        level = Level.INFO
    }
    install(WebSockets)

    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("MyCustomHeader")
        allowCredentials = true
        anyHost()
    }

    routing {
        get("/") {
            call.respondText("Hello, world!")
        }

        route("v1") {
            install(ContentNegotiation) {
                jackson {
                    setConfig(apiV1Mapper.serializationConfig)
                    setConfig(apiV1Mapper.deserializationConfig)
                }
            }
            v1Ticket(appSettings)
            webSocket("/ws") {
                wsHandlerV1(appSettings)
            }
        }
    }
}

