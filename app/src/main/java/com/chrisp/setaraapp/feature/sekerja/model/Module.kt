package com.chrisp.setaraapp.feature.sekerja.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Module(
    @SerialName("module_id")
    val moduleId: String, // Changed to camelCase
    val title: String,
    @SerialName("sessionInfo")
    val sessionInfo: String? = null, // Make nullable if not always present
    val topics: List<String>? = null, // Make nullable if not always present
    @SerialName("is_expanded") // Assuming this might come from DB or be a UI state
    var isExpanded: Boolean = false // For UI state, often managed in ViewModel or UI
)