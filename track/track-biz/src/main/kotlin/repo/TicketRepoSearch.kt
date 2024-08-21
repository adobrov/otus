package ru.otus.otuskotlin.track.biz.repo

import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.helpers.fail
import ru.otus.otuskotlin.track.common.models.TrackOperationState
import ru.otus.otuskotlin.track.common.repo.DbTicketsResponseErr
import ru.otus.otuskotlin.track.common.repo.DbTicketsResponseOk
import ru.otus.otuskotlin.track.common.repo.DbTicketFilterRequest
import ru.otus.otuskotlin.track.cor.ICorChainDsl
import ru.otus.otuskotlin.track.cor.worker

fun ICorChainDsl<TrackContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск заявок в БД по фильтру"
    on { operationState == TrackOperationState.RUNNING }
    handle {
        val request = DbTicketFilterRequest(
            titleFilter = ticketFilterValidated.searchString,
            ownerId = ticketFilterValidated.ownerId,
        )
        when(val result = ticketRepo.searchTicket(request)) {
            is DbTicketsResponseOk -> ticketsRepoDone = result.data.toMutableList()
            is DbTicketsResponseErr -> fail(result.errors)
        }
    }
}