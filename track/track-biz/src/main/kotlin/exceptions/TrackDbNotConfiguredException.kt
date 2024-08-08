package ru.otus.otuskotlin.track.biz.exceptions

import ru.otus.otuskotlin.track.common.models.TrackWorkMode

class TrackDbNotConfiguredException(val workMode: TrackWorkMode): Exception(
    "Database is not configured properly for work mode $workMode"
)