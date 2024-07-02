package ru.otus.otuskotlin.track.app.ktor

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class AppTest {
    @Test
    fun `root endpoint`() = testApplication {
        application { moduleJvm(TrackAppSettings()) }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Hello, world!", response.bodyAsText())
    }
}