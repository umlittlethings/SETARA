package com.chrisp.setaraapp.feature.jadwal.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Schedule(
    @SerialName("schedule_id")
    val scheduleId: String,

    @SerialName("course_id")
    val courseId: String,

    @SerialName("day")
    val day: String,

    @SerialName("start_time")
    val startTime: String,

    @SerialName("end_time")
    val endTime: String,

    @SerialName("method")
    val method: String, // "Online" or "Offline"

    @SerialName("location")
    val location: String? = null
)