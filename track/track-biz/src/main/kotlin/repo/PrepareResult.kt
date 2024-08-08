package ru.otus.otuskotlin.track.biz.repo

import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.models.TrackOperationState
import ru.otus.otuskotlin.track.common.models.TrackWorkMode
import ru.otus.otuskotlin.track.cor.ICorChainDsl
import ru.otus.otuskotlin.track.cor.worker

fun ICorChainDsl<TrackContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != TrackWorkMode.STUB }
    handle {
        ticketResponse = ticketRepoDone
        ticketsResponse = ticketsRepoDone
        operationState = when (val st = operationState) {
            TrackOperationState.RUNNING -> TrackOperationState.FINISHING
            else -> st
        }
    }
}