package ru.otus.otuskotlin.track.common.models

@JvmInline
value class TrackRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = TrackRequestId("")
    }
}