package ru.otus.otuskotlin.track.biz.validation

import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.models.TrackOperationState
import ru.otus.otuskotlin.track.cor.ICorChainDsl
import ru.otus.otuskotlin.track.cor.worker

fun ICorChainDsl<TrackContext>.finishTicketValidation(title: String) = worker {
    this.title = title
    on { operationState == TrackOperationState.RUNNING }
    handle {
        ticketValidated = ticketValidating
    }
}

fun ICorChainDsl<TrackContext>.finishTicketFilterValidation(title: String) = worker {
    this.title = title
    on { operationState == TrackOperationState.RUNNING }
    handle {
        ticketValidated = ticketValidating
    }
}