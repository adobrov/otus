package ru.otus.otuskotlin.track.common.ws


interface ITrackWsSessionRepo {
    fun add(session: ITrackWsSession)
    fun clearAll()
    fun remove(session: ITrackWsSession)
    suspend fun <K> sendAll(obj: K)

    companion object {
        val NONE = object : ITrackWsSessionRepo {
            override fun add(session: ITrackWsSession) {}
            override fun clearAll() {}
            override fun remove(session: ITrackWsSession) {}
            override suspend fun <K> sendAll(obj: K) {}
        }
    }
}