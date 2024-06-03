package ru.otus.otuskotlin.marketplace.mappers.v1

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.junit.Test
import ru.otus.otuskotlin.track.api.v1.models.*
import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.models.*
import ru.otus.otuskotlin.track.common.stubs.TrackStubs
import ru.otus.otuskotlin.track.mappers.v1.fromTransport
import ru.otus.otuskotlin.track.mappers.v1.toTransportCreate
import kotlin.test.assertEquals

class MappersTest {

    @Test
    fun fromTransport() {
        val createReq = TicketCreateRequest(
            debug = TicketDebug(
                mode = TicketRequestDebugMode.STUB,
                stub = TicketRequestDebugStubs.SUCCESS,
            ),
            ticket = TicketCreateObject(
                subject = "subject",
                description = "desc",
            ),
        )

        val addReq = TicketAddCommentRequest(
            debug = TicketDebug(
                mode = TicketRequestDebugMode.STUB,
                stub = TicketRequestDebugStubs.SUCCESS,
            ),
            comment = TicketAddCommentObject(
                owner = "owner",
                author = "author",
                text = "text",
                lock = 1,
            ),
        )

        val context = TrackContext()
        context.fromTransport(createReq)
        context.fromTransport(addReq)

        assertEquals(TrackStubs.SUCCESS, context.stubCase)
        assertEquals(TrackWorkMode.STUB, context.workMode)
        assertEquals("subject", context.ticketRequest.subject)
        assertEquals("text", context.newComment.text)
    }


    @Test
    fun toTransport() {
        val context = TrackContext(
            requestId = TrackRequestId("1234"),
            command = TrackCommand.CREATE,
            ticketResponse = TrackTicket(
                subject = "subject",
                description = "desc",
                creationDate = LocalDateTime.parse("2024-01-01T12:15:16").toInstant(TimeZone.UTC),
                comments = mutableListOf(
                    TrackTicketComment(
                        author = "author",
                        creationDate = LocalDateTime.parse("2024-02-01T12:15:16").toInstant(TimeZone.UTC),
                        text = "text",
                    ),
                ),
            ),
            errors = mutableListOf(
                TrackError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            operationState = TrackOperationState.RUNNING,
        )

        val req = context.toTransportCreate() as TicketCreateResponse

        assertEquals("subject", req.ticket?.subject)
        assertEquals("desc", req.ticket?.description)
        assertEquals("2024-01-01T12:15:16Z", req.ticket?.creationDate)
        assertEquals("text", req.ticket?.comment?.get(0)?.text)
        assertEquals("author", req.ticket?.comment?.get(0)?.author)
        assertEquals("2024-02-01T12:15:16Z", req.ticket?.comment?.get(0)?.creationDate)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}