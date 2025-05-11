package com.chrisp.setaraapp.feature.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.Flow

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val authManager = AuthManager(application.applicationContext)

    fun signInWithEmail(email: String, password: String): Flow<AuthResponse> {
        return authManager.signInWithEmail(email, password)
    }

    fun loginWithGoogle(): Flow<AuthResponse> {
        return authManager.loginGoogleUser()
    }

    fun signUpAndCreateProfileDirectly(
        email: String,
        password: String,
        fullName: String,
        birthDate: String,
        categoryDisability: String,
        phoneNumber: String,
        address: String
    ): Flow<AuthResponse> {
        return authManager.signUpAndCreateProfile(
            email,
            password,
            fullName,
            birthDate,
            categoryDisability,
            phoneNumber,
            address
        )
    }

    fun checkIfUserProfileExists(): Flow<Boolean> {
        return authManager.checkIfUserExists()
    }

    fun signUpWithGoogleAndCreateProfile(
        fullName: String,
        birthDate: String,
        categoryDisability: String,
        phoneNumber: String,
        address: String
    ): Flow<AuthResponse> {
        return authManager.signUpWithGoogleAndCreateProfile(
            fullName,
            birthDate,
            categoryDisability,
            phoneNumber,
            address
        )
    }

    fun logout(): Flow<AuthResponse> {
        return authManager.signOut()
    }
}