package repo

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.track.backend.repo.tests.TicketRepositoryMock
import ru.otus.otuskotlin.track.biz.TrackTicketProcessor
import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.TrackCorSettings
import ru.otus.otuskotlin.track.common.models.*
import ru.otus.otuskotlin.track.common.repo.DbTicketResponseOk
import ru.otus.otuskotlin.track.common.repo.errorNotFound
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private val initTicket = TrackTicket(
    id = TrackTicketId(123),
    subject = "abc",
    description = "abc",
)
private val repo = TicketRepositoryMock(
    invokeReadTicket = {
        if (it.id == initTicket.id) {
            DbTicketResponseOk(
                data = initTicket,
            )
        } else errorNotFound(it.id)
    }
)
private val settings = TrackCorSettings(repoTest = repo)
private val processor = TrackTicketProcessor(settings)

fun repoNotFoundTest(command: TrackCommand) = runTest {
    val ctx = TrackContext(
        command = command,
        operationState = TrackOperationState.NONE,
        workMode = TrackWorkMode.TEST,
        ticketRequest = TrackTicket(
            id = TrackTicketId(12345),
            subject = "xyz",
            description = "xyz",
            lock = TrackTicketLock(1),
        ),
    )
    processor.exec(ctx)
    assertEquals(TrackOperationState.FAILING, ctx.operationState)
    assertEquals(TrackTicket(), ctx.ticketResponse)
    assertEquals(1, ctx.errors.size)
    assertNotNull(ctx.errors.find { it.code == "repo-not-found" }, "Errors must contain not-found")
}