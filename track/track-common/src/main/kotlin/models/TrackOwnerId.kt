package ru.otus.otuskotlin.track.common.models


import kotlin.jvm.JvmInline
@JvmInline
value class TrackOwnerId (private val id: String) {
    fun asString() = id

    companion object {
        val NONE = TrackOwnerId("")
    }
}