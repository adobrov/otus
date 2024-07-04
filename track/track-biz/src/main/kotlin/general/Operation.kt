package ru.otus.otuskotlin.track.biz.general

import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.models.TrackCommand
import ru.otus.otuskotlin.track.common.models.TrackOperationState
import ru.otus.otuskotlin.track.cor.ICorChainDsl
import ru.otus.otuskotlin.track.cor.chain

fun ICorChainDsl<TrackContext>.operation(
    title: String,
    command: TrackCommand,
    block: ICorChainDsl<TrackContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && operationState == TrackOperationState.RUNNING }
}