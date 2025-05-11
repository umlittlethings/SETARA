package com.chrisp.setaraapp.Model.DataAuth.Repository

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Module(
    val module_id: String,
    val title: String,
    val sesi: Int,
    val content: List<String>
)