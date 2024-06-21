package ru.otus.otuskotlin.track.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class TrackTicketId (private val id: Int) {
    fun asInt() = id

    companion object {
        val NONE = TrackTicketId(0)
    }
}