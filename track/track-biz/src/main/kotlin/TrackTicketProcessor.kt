package ru.otus.otuskotlin.track.biz

import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.TrackCorSettings
import ru.otus.otuskotlin.track.common.models.TrackOperationState
import ru.otus.otuskotlin.track.stubs.TrackTicketStub


@Suppress("unused", "RedundantSuspendModifier")
class TrackTicketProcessor(val corSettings: TrackCorSettings) {
    suspend fun exec(ctx: TrackContext) {
        ctx.ticketResponse = TrackTicketStub.get()
        ctx.operationState = TrackOperationState.RUNNING
        ctx.ticketsResponse = TrackTicketStub.prepareSearchList()
    }
}