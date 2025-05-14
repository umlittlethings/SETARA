package com.chrisp.setaraapp.feature.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.Flow

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = Repository(application.applicationContext)

    fun signInWithEmail(email: String, password: String): Flow<AuthResponse> {
        return repository.signInWithEmail(email, password)
    }

    fun loginWithGoogle(): Flow<AuthResponse> {
        return repository.loginGoogleUser()
    }

    fun isUserLoggedIn(): Flow<Boolean> {
        return repository.isUserLoggedIn()
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
        return repository.signUpAndCreateProfile(
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
        return repository.checkIfUserExists()
    }

    fun signUpWithGoogleAndCreateProfile(
        fullName: String,
        birthDate: String,
        categoryDisability: String,
        phoneNumber: String,
        address: String
    ): Flow<AuthResponse> {
        return repository.signUpWithGoogleAndCreateProfile(
            fullName,
            birthDate,
            categoryDisability,
            phoneNumber,
            address
        )
    }

    fun logout(): Flow<AuthResponse> {
        return repository.signOut()
    }
}