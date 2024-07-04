package ru.otus.otuskotlin.track.biz.validation

import ru.otus.otuskotlin.track.cor.ICorChainDsl
import ru.otus.otuskotlin.track.cor.worker
import ru.otus.otuskotlin.track.common.helpers.errorValidation
import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.helpers.fail

fun ICorChainDsl<TrackContext>.validateIdNotEmpty(title: String) = worker {
    this.title = title
    on { ticketValidating.id.asInt() == 0 }
    handle {
        fail(
            errorValidation(
                field = "id",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}