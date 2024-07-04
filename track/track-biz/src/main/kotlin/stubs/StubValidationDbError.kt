package ru.otus.otuskotlin.track.biz.stubs

import ru.otus.otuskotlin.track.cor.ICorChainDsl
import ru.otus.otuskotlin.track.cor.worker
import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.helpers.fail
import ru.otus.otuskotlin.track.common.models.TrackError
import ru.otus.otuskotlin.track.common.models.TrackOperationState
import ru.otus.otuskotlin.track.common.stubs.TrackStubs

fun ICorChainDsl<TrackContext>.stubDbError(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки базы данных
    """.trimIndent()
    on { stubCase == TrackStubs.DB_ERROR && operationState == TrackOperationState.RUNNING }
    handle {
        fail(
            TrackError(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}