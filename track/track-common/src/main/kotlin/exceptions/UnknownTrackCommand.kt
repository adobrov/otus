package ru.otus.otuskotlin.track.common.exceptions

import ru.otus.otuskotlin.track.common.models.TrackCommand

class UnknownTrackCommand(command: TrackCommand) : Throwable("Wrong command $command at mapping toTransport stage")