package ru.otus.otuskotlin.track.logging.logback

import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.track.logging.common.ITrackLogWrapper
import kotlin.reflect.KClass


fun tLoggerLogback(logger: Logger): ITrackLogWrapper = TrackLogWrapperLogback(
    logger = logger,
    loggerId = logger.name,
)

fun tLoggerLogback(clazz: KClass<*>): ITrackLogWrapper = tLoggerLogback(LoggerFactory.getLogger(clazz.java) as Logger)

@Suppress("unused")
fun tLoggerLogback(loggerId: String): ITrackLogWrapper = tLoggerLogback(LoggerFactory.getLogger(loggerId) as Logger)
