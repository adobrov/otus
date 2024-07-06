package ru.otus.otuskotlin.track.biz.stubs

import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.models.TrackOperationState
import ru.otus.otuskotlin.track.common.models.TrackWorkMode
import ru.otus.otuskotlin.track.cor.ICorChainDsl
import ru.otus.otuskotlin.track.cor.chain

fun ICorChainDsl<TrackContext>.stubs(title: String, block: ICorChainDsl<TrackContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == TrackWorkMode.STUB && operationState == TrackOperationState.RUNNING }
}