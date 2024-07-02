package ru.otus.otuskotlin.track.app.ktor.base

import ru.otus.otuskotlin.track.common.ws.ITrackWsSession
import ru.otus.otuskotlin.track.common.ws.ITrackWsSessionRepo

class KtorWsSessionRepo: ITrackWsSessionRepo {
    private val sessions: MutableSet<ITrackWsSession> = mutableSetOf()
    override fun add(session: ITrackWsSession) {
        sessions.add(session)
    }

    override fun clearAll() {
        sessions.clear()
    }

    override fun remove(session: ITrackWsSession) {
        sessions.remove(session)
    }

    override suspend fun <T> sendAll(obj: T) {
        sessions.forEach { it.send(obj) }
    }
}