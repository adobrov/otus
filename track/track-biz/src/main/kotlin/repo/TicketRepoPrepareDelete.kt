package ru.otus.otuskotlin.track.biz.repo

import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.models.TrackOperationState
import ru.otus.otuskotlin.track.cor.ICorChainDsl
import ru.otus.otuskotlin.track.cor.worker

fun ICorChainDsl<TrackContext>.repoPrepareDelete(title: String) = worker {
    this.title = title
    description = """
        Готовим данные к удалению из БД
    """.trimIndent()
    on { operationState == TrackOperationState.RUNNING }
    handle {
        ticketRepoPrepare = ticketValidated.deepCopy()
    }
}