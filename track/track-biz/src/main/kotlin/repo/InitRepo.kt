package ru.otus.otuskotlin.track.biz.repo

import ru.otus.otuskotlin.track.biz.exceptions.TrackDbNotConfiguredException
import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.helpers.errorSystem
import ru.otus.otuskotlin.track.common.helpers.fail
import ru.otus.otuskotlin.track.common.models.TrackWorkMode
import ru.otus.otuskotlin.track.common.repo.IRepoTicket
import ru.otus.otuskotlin.track.cor.ICorChainDsl
import ru.otus.otuskotlin.track.cor.worker

fun ICorChainDsl<TrackContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от запрошенного режима работы        
    """.trimIndent()
    handle {
        ticketRepo = when {
            workMode == TrackWorkMode.TEST -> corSettings.repoTest
            workMode == TrackWorkMode.STUB -> corSettings.repoStub
            else -> corSettings.repoProd
        }
        if (workMode != TrackWorkMode.STUB && ticketRepo == IRepoTicket.NONE) fail(
            errorSystem(
                violationCode = "dbNotConfigured",
                e = TrackDbNotConfiguredException(workMode)
            )
        )
    }
}