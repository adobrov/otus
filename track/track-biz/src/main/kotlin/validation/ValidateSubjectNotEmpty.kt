package ru.otus.otuskotlin.track.biz.validation

import ru.otus.otuskotlin.track.cor.ICorChainDsl
import ru.otus.otuskotlin.track.cor.worker
import ru.otus.otuskotlin.track.common.helpers.errorValidation
import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.helpers.fail

fun ICorChainDsl<TrackContext>.validateTitleNotEmpty(title: String) = worker {
    this.title = title
    on { ticketValidating.subject.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "subject",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}