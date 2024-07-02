package ru.otus.otuskotlin.track.app.common

import kotlinx.datetime.Clock
import ru.otus.otuskotlin.track.api.log.mapper.toLog
import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.helpers.asTrackError
import ru.otus.otuskotlin.track.common.models.TrackCommand
import ru.otus.otuskotlin.track.common.models.TrackOperationState
import kotlin.reflect.KClass

suspend inline fun <T> ITrackAppSettings.controllerHelper(
    crossinline getRequest: suspend TrackContext.() -> Unit,
    crossinline toResponse: suspend TrackContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = TrackContext(
        timeStart = Clock.System.now(),
    )
    return try {
        logger.info(
            msg = "Request $logId started for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.getRequest()
        processor.exec(ctx)
        logger.info(
            msg = "Request $logId processed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.toResponse()
    } catch (e: Throwable) {
        logger.error(
            msg = "Request $logId failed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId),
            e = e,
        )
        ctx.operationState = TrackOperationState.FAILING
        ctx.errors.add(e.asTrackError())
        processor.exec(ctx)
        if (ctx.command == TrackCommand.NONE) {
            ctx.command = TrackCommand.READ
        }
        ctx.toResponse()
    }
}
