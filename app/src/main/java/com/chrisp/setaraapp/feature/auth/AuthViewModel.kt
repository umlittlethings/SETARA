package com.chrisp.setaraapp.feature.auth

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(application.applicationContext)

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isUserLoading = mutableStateOf(false)
    val isUserLoading: State<Boolean> = _isUserLoading

    private val _userError = mutableStateOf<String?>(null)
    val userError: State<String?> = _userError

    init {
        observeUserLoginStatus()
    }

    private fun observeUserLoginStatus() {
        authRepository.isUserLoggedIn().onEach { isLoggedIn ->
            if (isLoggedIn) {
                fetchCurrentUserProfile()
            } else {
                _currentUser.value = null // Clear user on logout
            }
        }.launchIn(viewModelScope)
    }

    fun fetchCurrentUserProfile() {
        viewModelScope.launch {
            _isUserLoading.value = true
            _userError.value = null
            authRepository.getUser().collect { result ->
                result.fold(
                    onSuccess = { user ->
                        _currentUser.value = user
                        _isUserLoading.value = false
                        Log.d("AuthViewModel", "User profile fetched: ${user.f_name}")
                    },
                    onFailure = { exception ->
                        _currentUser.value = null
                        _isUserLoading.value = false
                        _userError.value = "Gagal memuat profil pengguna: ${exception.message}"
                        Log.e("AuthViewModel", "Error fetching user profile", exception)
                    }
                )
            }
        }
    }

    fun signInWithEmail(email: String, password: String): Flow<AuthResponse> {
        // After successful sign-in, trigger profile fetch
        return kotlinx.coroutines.flow.channelFlow { // Use channelFlow to combine
            authRepository.signInWithEmail(email, password).collect { response ->
                send(response) // Forward original response
                if (response is AuthResponse.Success) {
                    fetchCurrentUserProfile()
                }
            }
        }
    }

    fun loginWithGoogle(): Flow<AuthResponse> {
        return kotlinx.coroutines.flow.channelFlow {
            authRepository.loginGoogleUser().collect { response ->
                send(response) // Forward original response
                if (response is AuthResponse.Success) {
                    fetchCurrentUserProfile()
                }
            }
        }
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
        return kotlinx.coroutines.flow.channelFlow {
            authRepository.signUpAndCreateProfile(
                email, password, fullName, birthDate, categoryDisability, phoneNumber, address
            ).collect { response ->
                send(response)
                if (response is AuthResponse.Success) {
                    fetchCurrentUserProfile()
                }
            }
        }
    }

    fun signUpWithGoogleAndCreateProfile(
        fullName: String,
        birthDate: String,
        categoryDisability: String,
        phoneNumber: String,
        address: String
    ): Flow<AuthResponse> {
        return kotlinx.coroutines.flow.channelFlow {
            authRepository.signUpWithGoogleAndCreateProfile(
                fullName, birthDate, categoryDisability, phoneNumber, address
            ).collect { response ->
                send(response)
                if (response is AuthResponse.Success) {
                    fetchCurrentUserProfile()
                }
            }
        }
    }


    fun logout() {
        viewModelScope.launch {
            authRepository.signOut().collect { response ->
                when (response) {
                    is AuthResponse.Success -> {
                        Log.d("AuthViewModel", "Logout successful from repository")
                        _currentUser.value = null // Clear user on logout
                    }
                    is AuthResponse.Error -> {
                        Log.e("AuthViewModel", "Logout error: ${response.message}")
                    }
                    is AuthResponse.Loading -> { /* No action needed here for currentUser state */ }
                }
            }
        }
    }

    fun isUserLoggedIn(): Flow<Boolean> {
        return authRepository.isUserLoggedIn()
    }

    fun checkIfUserProfileExists(): Flow<Boolean> {
        return authRepository.checkIfUserExists()
    }
}