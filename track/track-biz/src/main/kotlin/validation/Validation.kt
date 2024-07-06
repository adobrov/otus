package ru.otus.otuskotlin.track.biz.validation

import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.models.TrackOperationState
import ru.otus.otuskotlin.track.cor.ICorChainDsl
import ru.otus.otuskotlin.track.cor.chain

fun ICorChainDsl<TrackContext>.validation(block: ICorChainDsl<TrackContext>.() -> Unit) = chain {
    block()
    title = "Валидация"

    on { operationState == TrackOperationState.RUNNING }
}