package ru.otus.otuskotlin.track.biz.repo

import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.helpers.fail
import ru.otus.otuskotlin.track.common.models.TrackOperationState
import ru.otus.otuskotlin.track.common.repo.DbTicketRequest
import ru.otus.otuskotlin.track.common.repo.DbTicketResponseErr
import ru.otus.otuskotlin.track.common.repo.DbTicketResponseErrWithData
import ru.otus.otuskotlin.track.common.repo.DbTicketResponseOk
import ru.otus.otuskotlin.track.cor.ICorChainDsl
import ru.otus.otuskotlin.track.cor.worker

fun ICorChainDsl<TrackContext>.repoUpdate(title: String) = worker {
    this.title = title
    on { operationState == TrackOperationState.RUNNING }
    handle {
        val request = DbTicketRequest(ticketRepoPrepare)
        when(val result = ticketRepo.updateTicket(request)) {
            is DbTicketResponseOk -> ticketRepoDone = result.data
            is DbTicketResponseErr -> fail(result.errors)
            is DbTicketResponseErrWithData -> {
                fail(result.errors)
                ticketRepoDone = result.data
            }
        }
    }
}