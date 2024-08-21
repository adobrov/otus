package ru.otus.otuskotlin.track.biz.repo

import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.helpers.fail
import ru.otus.otuskotlin.track.common.models.TrackOperationState
import ru.otus.otuskotlin.track.common.repo.DbTicketIdRequest
import ru.otus.otuskotlin.track.common.repo.DbTicketResponseErr
import ru.otus.otuskotlin.track.common.repo.DbTicketResponseErrWithData
import ru.otus.otuskotlin.track.common.repo.DbTicketResponseOk
import ru.otus.otuskotlin.track.cor.ICorChainDsl
import ru.otus.otuskotlin.track.cor.worker

fun ICorChainDsl<TrackContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение заявки из БД"
    on { operationState == TrackOperationState.RUNNING }
    handle {
        val request = DbTicketIdRequest(ticketValidated)
        when(val result = ticketRepo.readTicket(request)) {
            is DbTicketResponseOk -> ticketRepoRead = result.data
            is DbTicketResponseErr -> fail(result.errors)
            is DbTicketResponseErrWithData -> {
                fail(result.errors)
                ticketRepoRead = result.data
            }
        }
    }
}