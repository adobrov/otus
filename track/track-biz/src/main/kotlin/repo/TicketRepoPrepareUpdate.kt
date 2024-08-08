package ru.otus.otuskotlin.track.biz.repo

import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.models.TrackOperationState
import ru.otus.otuskotlin.track.cor.ICorChainDsl
import ru.otus.otuskotlin.track.cor.worker

fun ICorChainDsl<TrackContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { operationState == TrackOperationState.RUNNING }
    handle {
        ticketRepoPrepare = ticketRepoRead.deepCopy().apply {
            this.subject = ticketValidated.subject
            description = ticketValidated.description
        }
    }
}