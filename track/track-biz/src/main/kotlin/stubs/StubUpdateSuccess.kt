package ru.otus.otuskotlin.track.biz.stubs

import ru.otus.otuskotlin.track.cor.ICorChainDsl
import ru.otus.otuskotlin.track.cor.worker
import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.TrackCorSettings
import ru.otus.otuskotlin.track.common.models.TrackTicketId
import ru.otus.otuskotlin.track.common.models.TrackOperationState
import ru.otus.otuskotlin.track.common.stubs.TrackStubs
import ru.otus.otuskotlin.track.logging.common.LogLevel
import ru.otus.otuskotlin.track.stubs.TrackTicketStub

fun ICorChainDsl<TrackContext>.stubUpdateSuccess(title: String, corSettings: TrackCorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для изменения объявления
    """.trimIndent()
    on { stubCase == TrackStubs.SUCCESS && operationState == TrackOperationState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubOffersSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            operationState = TrackOperationState.FINISHING
            val stub = TrackTicketStub.prepareResult {
                ticketRequest.id.takeIf { it != TrackTicketId.NONE }?.also { this.id = it }
                ticketRequest.subject.takeIf { it.isNotBlank() }?.also { this.subject = it }
                ticketRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
            }
            ticketResponse = stub
        }
    }
}