package ru.otus.otuskotlin.track.common.helpers


import ru.otus.otuskotlin.track.common.TrackContext
import ru.otus.otuskotlin.track.common.models.TrackError
import ru.otus.otuskotlin.track.common.models.TrackOperationState
import ru.otus.otuskotlin.track.logging.common.LogLevel

fun Throwable.asTrackError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = TrackError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

inline fun TrackContext.addError(vararg error: TrackError) = errors.addAll(error)
inline fun TrackContext.addErrors(error: Collection<TrackError>) = errors.addAll(error)

inline fun TrackContext.fail(error: TrackError) {
    addError(error)
    operationState = TrackOperationState.FAILING
}

inline fun TrackContext.fail(errors: Collection<TrackError>) {
    addErrors(errors)
    operationState = TrackOperationState.FAILING
}


inline fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
) = TrackError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)

inline fun errorSystem(
    violationCode: String,
    level: LogLevel = LogLevel.ERROR,
    e: Throwable,
) = TrackError(
    code = "system-$violationCode",
    group = "system",
    message = "System error occurred. Our stuff has been informed, please retry later",
    level = level,
    exception = e,
)