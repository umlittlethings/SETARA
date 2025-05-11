package com.chrisp.setaraapp.Model.DataAuth.Repository

import java.util.UUID

data class Course(
    val id: String,
    val title: String,
    val description: String,
    val modules: List<Module> = emptyList()
)
