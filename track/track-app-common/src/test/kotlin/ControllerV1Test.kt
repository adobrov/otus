package ru.otus.otuskotlin.track.app.common

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.track.mappers.v1.fromTransport
import ru.otus.otuskotlin.track.mappers.v1.toTransportCreate
import ru.otus.otuskotlin.track.api.v1.models.*
import ru.otus.otuskotlin.track.biz.TrackTicketProcessor
import ru.otus.otuskotlin.track.common.TrackCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class ControllerV1Test {

    private val request = TicketCreateRequest(
        ticket = TicketCreateObject(
            subject = "some ad",
            description = "some description of some ad",

        ),
        debug = TicketDebug(mode = TicketRequestDebugMode.STUB, stub = TicketRequestDebugStubs.SUCCESS)
    )

    private val appSettings: ITrackAppSettings = object : ITrackAppSettings {
        override val corSettings: TrackCorSettings = TrackCorSettings()
        override val processor: TrackTicketProcessor = TrackTicketProcessor(corSettings)
    }


    class TestApplicationCall(private val request: IRequest) {
        var res: IResponse? = null

        @Suppress("UNCHECKED_CAST")
        fun <T : IRequest> receive(): T = request as T
        fun respond(res: IResponse) {
            this.res = res
        }
    }

    private suspend fun TestApplicationCall.createAdKtor(appSettings: ITrackAppSettings) {
        val resp = appSettings.controllerHelper(
            { fromTransport(receive<TicketCreateRequest>()) },
            { toTransportCreate() },
            ControllerV1Test::class,
            "controller-v1-test"
        )
        respond(resp)
    }

    @Test
    fun ktorHelperTest() = runTest {
        val testApp = TestApplicationCall(request).apply { createAdKtor(appSettings) }
        val res = testApp.res as TicketCreateResponse
        assertEquals(ResponseResult.SUCCESS, res.result)
    }
}
