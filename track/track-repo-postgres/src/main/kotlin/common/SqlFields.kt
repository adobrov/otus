package ru.otus.otuskotlin.track.backend.repo.postgresql

object TicketSqlFields {
    const val ID = "id"
    const val SUBJECT = "subject"
    const val DESCRIPTION = "description"
    const val STATE = "state"

    const val CREATION_DATE = "creation_date"
    const val FINISH_DATE = "finish_date"

    const val LOCK = "lock"
    const val LOCK_OLD = "lock_old"
    const val OWNER_ID = "owner_id"

    const val STATE_TYPE = "ticket_state_type"
    const val STATE_NEW = "new"
    const val STATE_PROGRESS = "progress"
    const val STATE_FINISH = "finish"

    const val FILTER_SUBJECT = SUBJECT
    const val FILTER_OWNER_ID = OWNER_ID

    const val DELETE_OK = "DELETE_OK"
    fun String.quoted() = "\"$this\""
    val allFields = listOf(
        ID, SUBJECT, DESCRIPTION, LOCK, OWNER_ID, CREATION_DATE, FINISH_DATE
    )
}

object CommentSqlFields {
    const val ID = "id"
    const val TICKET_ID = "ticket_id"
    const val AUTHOR = "author"
    const val CREATION_DATE = "creation_date"
    const val TEXT = "text"

    fun String.quoted() = "\"$this\""
    val allFields = listOf(
        ID, TICKET_ID, AUTHOR, CREATION_DATE, TEXT
    )
}