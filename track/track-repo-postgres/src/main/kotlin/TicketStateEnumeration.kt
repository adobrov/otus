package ru.otus.otuskotlin.track.backend.repo.postgresql

import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject
import ru.otus.otuskotlin.track.common.models.TrackState

fun Table.ticketStateEnumeration(
    columnName: String
) = customEnumeration(
    name = columnName,
    sql = TicketSqlFields.STATE_TYPE,
    fromDb = { value ->
        when (value.toString()) {
            TicketSqlFields.STATE_NEW -> TrackState.NEW
            TicketSqlFields.STATE_PROGRESS -> TrackState.PROGRESS
            TicketSqlFields.STATE_FINISH -> TrackState.FINISH
            else -> TrackState.NONE
        }
    },
    toDb = { value ->
        when (value) {
            TrackState.NEW -> PgTicketStateNew
            TrackState.PROGRESS -> PgTicketStateProgress
            TrackState.FINISH -> PgTicketStateFinish
            TrackState.NONE -> throw Exception("Wrong value of ticket Type. NONE is unsupported")
        }
    }
)

sealed class PgTicketStateValue(enVal: String): PGobject() {
    init {
        type = TicketSqlFields.STATE_TYPE
        value = enVal
    }
}

object PgTicketStateNew: PgTicketStateValue(TicketSqlFields.STATE_NEW) {
    private fun readResolve(): Any = PgTicketStateNew
}
object PgTicketStateProgress: PgTicketStateValue(TicketSqlFields.STATE_PROGRESS) {
    private fun readResolve(): Any = PgTicketStateProgress
}
object PgTicketStateFinish: PgTicketStateValue(TicketSqlFields.STATE_FINISH) {
    private fun readResolve(): Any = PgTicketStateFinish
}
