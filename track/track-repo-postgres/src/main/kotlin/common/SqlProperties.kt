package ru.otus.otuskotlin.track.backend.repo.postgresql

data class SqlProperties(
    val host: String = "127.0.0.1",
    val port: Int = 5432,
    val user: String = "trackUser",
    val password: String = "123456",
    val database: String = "track",
    val schema: String = "public",
    val ticketTable: String = "tickets",
    val commentTable: String = "comments",
) {
    val url: String
        get() = "jdbc:postgresql://${host}:${port}/${database}"
}