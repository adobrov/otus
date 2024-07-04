package ru.otus.otuskotlin.track.biz.validation

import ru.otus.otuskotlin.track.common.helpers.errorValidation
import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.helpers.fail
import ru.otus.otuskotlin.track.cor.ICorChainDsl
import ru.otus.otuskotlin.track.cor.worker

fun ICorChainDsl<TrackContext>.validateLockNotEmpty(title: String) = worker {
    this.title = title
    on { ticketValidating.lock.asInt() == 0 }
    handle {
        fail(
            errorValidation(
                field = "lock",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}