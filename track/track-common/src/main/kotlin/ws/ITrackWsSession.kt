package ru.otus.otuskotlin.track.common.ws

interface ITrackWsSession {
    suspend fun <T> send(obj: T)
    companion object {
        val NONE = object : ITrackWsSession {
            override suspend fun <T> send(obj: T) {
            }
        }
    }
}