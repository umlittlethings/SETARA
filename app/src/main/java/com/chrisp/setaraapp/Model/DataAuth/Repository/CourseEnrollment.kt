package com.chrisp.setaraapp.Model.DataAuth.Repository

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class Daftar(
    @SerialName("user_id")
    val userId: String,

    @SerialName("course_id")
    val courseId: String,

    val completed: Boolean = false,
    val progress: Float = 0f,

    @SerialName("last_opened")
    val lastOpened: String? = null
)