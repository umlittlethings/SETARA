package com.chrisp.setaraapp.feature.sekerja.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseEnrollment(
    @SerialName("enrollment_id")
    val enrollmentId: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("course_id")
    val courseId: String,
    val completed: Boolean = false,
    val progress: Float = 0f,
    @SerialName("last_opened")
    val lastOpened: String? = null
)