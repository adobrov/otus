package ru.otus.otuskotlin.track.biz.stubs

import ru.otus.otuskotlin.track.cor.ICorChainDsl
import ru.otus.otuskotlin.track.cor.worker
import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.helpers.fail
import ru.otus.otuskotlin.track.common.models.TrackError
import ru.otus.otuskotlin.track.common.models.TrackOperationState
import ru.otus.otuskotlin.track.common.stubs.TrackStubs

fun ICorChainDsl<TrackContext>.stubValidationBadDescription(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для описания объявления
    """.trimIndent()
    on { stubCase == TrackStubs.BAD_DESCRIPTION && operationState == TrackOperationState.RUNNING }
    handle {
        fail(
            TrackError(
                group = "validation",
                code = "validation-description",
                field = "description",
                message = "Wrong description field"
            )
        )
    }
}