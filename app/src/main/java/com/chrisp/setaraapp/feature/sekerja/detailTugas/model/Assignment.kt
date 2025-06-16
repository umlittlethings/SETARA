package com.chrisp.setaraapp.feature.sekerja.detailTugas.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Assignment(
    @SerialName("assignment_id")
    val assignmentId: String,

    @SerialName("course_id")
    val courseId: String,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("title")
    val title: String,

    @SerialName("description")
    val description: String,

    @SerialName("deadline")
    val deadline: String,

    @SerialName("type")
    val type: String,

    @SerialName("syarat")
    val syarat: List<String>
)