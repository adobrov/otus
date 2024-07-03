package ru.otus.otuskotlin.track.app.ktor.base


import io.ktor.websocket.*
import ru.otus.otuskotlin.track.api.v1.apiV1ResponseSerialize
import ru.otus.otuskotlin.track.api.v1.models.IResponse
import ru.otus.otuskotlin.track.common.ws.ITrackWsSession

data class KtorWsSessionV1(
    private val session: WebSocketSession
) : ITrackWsSession {
    override suspend fun <T> send(obj: T) {
        require(obj is IResponse)
        session.send(Frame.Text(apiV1ResponseSerialize(obj)))
    }
}