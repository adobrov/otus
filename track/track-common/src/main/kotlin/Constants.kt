package ru.otus.otuskotlin.track.common

import kotlinx.datetime.Instant

private val INSTANT_NONE = Instant.fromEpochMilliseconds(0)
val Instant.Companion.NONE
    get() = INSTANT_NONE
