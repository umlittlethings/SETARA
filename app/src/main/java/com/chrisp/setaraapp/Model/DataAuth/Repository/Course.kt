package com.chrisp.setaraapp.Model.DataAuth.Repository

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Course(
    @SerialName("course_id")
    val course_id: String,
    val title: String,
    val detail: String,
    val modules: List<Module> = emptyList()
)
