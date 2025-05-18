package com.chrisp.setaraapp.feature.sekerja.detailProgram

// --- Dummy Data ---
data class CurriculumModule(
    val id: String,
    val title: String,
    val sessionInfo: String,
    val topics: List<String>,
    var isExpanded: Boolean = false
)