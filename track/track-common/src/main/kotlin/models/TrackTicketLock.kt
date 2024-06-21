package ru.otus.otuskotlin.track.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class TrackTicketLock(private val id: Int) {
    fun asInt() = id

    companion object {
        val NONE = TrackTicketLock(0)
    }
}