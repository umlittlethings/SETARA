package com.chrisp.setaraapp.feature.auth

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

sealed interface AuthResponse {
    data object Success : AuthResponse
    data class Error(val message: String?) : AuthResponse
    data object Loading : AuthResponse
}

@Serializable
data class User(
    val id: String = "",
    val f_name: String,
    val email: String,
    val birth_date: String,
    val cat_disability: String,
    val no_telp: String,
    val address: String,
    val created_at: String = LocalDateTime.toString()
)
