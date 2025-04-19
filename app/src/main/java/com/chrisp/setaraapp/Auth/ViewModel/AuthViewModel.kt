package com.chrisp.setaraapp.Auth.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.chrisp.setaraapp.Model.DataAuth.AuthManager
import com.chrisp.setaraapp.Model.DataAuth.AuthResponse
import com.chrisp.setaraapp.Model.DataAuth.UserProfile
import kotlinx.coroutines.flow.Flow

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val authManager = AuthManager(application.applicationContext)

    fun signInWithEmail(email: String, password: String): Flow<AuthResponse> {
        return authManager.signInWithEmail(email, password)
    }

    fun signUpWithEmail(email: String, password: String): Flow<AuthResponse> {
        return authManager.signUpWithEmail(email, password)
    }

    fun loginWithGoogle(): Flow<AuthResponse> {
        return authManager.loginGoogleUser()
    }

    fun signUpWithEmailAndCreateProfile(
        email: String,
        password: String,
        fullName: String,
        birthDate: String,
        categoryDisability: String,
        phoneNumber: String,
        address: String
    ): Flow<AuthResponse> {
        return authManager.signUpWithEmailAndCreateProfile(
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
        return authManager.checkIfUserProfileExists()
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






}