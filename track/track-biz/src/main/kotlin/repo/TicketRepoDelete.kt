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

fun ICorChainDsl<TrackContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление заявки из БД по ID"
    on { operationState == TrackOperationState.RUNNING }
    handle {
        val request = DbTicketIdRequest(ticketRepoPrepare)
        when(val result = ticketRepo.deleteTicket(request)) {
            is DbTicketResponseOk -> ticketRepoDone = result.data
            is DbTicketResponseErr -> {
                fail(result.errors)
                ticketRepoDone = ticketRepoRead
            }
            is DbTicketResponseErrWithData -> {
                fail(result.errors)
                ticketRepoDone = result.data
            }
        }
    }
}