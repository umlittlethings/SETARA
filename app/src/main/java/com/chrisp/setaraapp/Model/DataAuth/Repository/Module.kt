package com.chrisp.setaraapp.Model.DataAuth.Repository

import java.util.UUID

data class Module(
    val id: String,
    val title: String,
    val sesi: Int,
    val content: List<String>
)