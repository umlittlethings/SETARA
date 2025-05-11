package com.chrisp.setaraapp.Model.DataAuth.Repository

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseModule(
    @SerialName("course_id") val courseId: String,
    @SerialName("module_id") val moduleId: String,
    @SerialName("order_num") val orderNum: Int
)