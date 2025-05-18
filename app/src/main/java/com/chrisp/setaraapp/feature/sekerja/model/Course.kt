package com.chrisp.setaraapp.feature.sekerja.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Course(
    @SerialName("course_id")
    val courseId: String,
    val title: String,
    val company: String,
    val periode: String,
    @SerialName("disability_tag")
    val disabilityTag: String,
    val detail: String,
    val modules: List<Module> = emptyList()
)