package com.chrisp.setaraapp.feature.sekerja.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseEnrollment(
    @SerialName("daftar_id")
    val enrollmentId: String? = null,
    @SerialName("user_id")
    val userId: String,
    @SerialName("course_id")
    val courseId: String,
    @SerialName("completed")
    val completed: Boolean = false,
    @SerialName("progress")
    val progress: Float = 0f,
    @SerialName("last_opened")
    val lastOpened: String? = null
)