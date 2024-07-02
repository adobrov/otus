package ru.otus.otuskotlin.track.app.ktor


import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import kotlinx.coroutines.withTimeout
import ru.otus.otuskotlin.track.api.v1.models.*
import ru.otus.otuskotlin.track.app.ktor.TrackAppSettings
import ru.otus.otuskotlin.track.app.ktor.moduleJvm
import ru.otus.otuskotlin.track.common.TrackCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class WebsocketStubTest {

    @Test
    fun createStub() {
        val request = TicketCreateRequest(
            ticket = TicketCreateObject(
                subject = "subj",
                description = "descr",
            ),
            debug = TicketDebug(
                mode = TicketRequestDebugMode.STUB,
                stub = TicketRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun readStub() {
        val request = TicketReadRequest(
            ticket = TicketReadObject(666),
            debug = TicketDebug(
                mode = TicketRequestDebugMode.STUB,
                stub = TicketRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun updateStub() {
        val request = TicketUpdateRequest(
            ticket = TicketUpdateObject(
                id = 666,
                subject = "subj",
                description = "descr",
            ),
            debug = TicketDebug(
                mode = TicketRequestDebugMode.STUB,
                stub = TicketRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun deleteStub() {
        val request = TicketDeleteRequest(
            ticket = TicketDeleteObject(
                id = 666,
            ),
            debug = TicketDebug(
                mode = TicketRequestDebugMode.STUB,
                stub = TicketRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun searchStub() {
        val request = TicketSearchRequest(
            filter = TicketSearchFilterObject(),
            debug = TicketDebug(
                mode = TicketRequestDebugMode.STUB,
                stub = TicketRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    private inline fun <reified T> testMethod(
        request: IRequest,
        crossinline assertBlock: (T) -> Unit
    ) = testApplication {
        application { moduleJvm(TrackAppSettings(corSettings = TrackCorSettings())) }
        val client = createClient {
            install(WebSockets) {
                contentConverter = JacksonWebsocketContentConverter()
            }
        }

        client.webSocket("/v1/ws") {
            withTimeout(3000) {
                val response = receiveDeserialized<IResponse>() as T
                assertIs<TicketInitResponse>(response)
            }
            sendSerialized(request)
            withTimeout(3000) {
                val response = receiveDeserialized<IResponse>() as T
                assertBlock(response)
            }
        }
    }
}