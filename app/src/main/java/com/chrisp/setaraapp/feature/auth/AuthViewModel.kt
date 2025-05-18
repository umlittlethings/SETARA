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

    // Expose user data
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // Expose a simple loading state for user fetching if needed
    private val _isUserLoading = mutableStateOf(false)
    val isUserLoading: State<Boolean> = _isUserLoading

    // Expose a simple error state for user fetching
    private val _userError = mutableStateOf<String?>(null)
    val userError: State<String?> = _userError

    init {
        // Optionally, fetch user data when ViewModel is created if user might already be logged in
        // This depends on how you manage session persistence and initial checks.
        // For now, we'll make it an explicit call or trigger it after login.
        observeUserLoginStatus()
    }

    private fun observeUserLoginStatus() {
        // If you want to automatically fetch profile when login status changes
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


    // ... other existing functions ...

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
        // After successful Google login, trigger profile fetch
        return kotlinx.coroutines.flow.channelFlow {
            authRepository.loginGoogleUser().collect { response ->
                send(response) // Forward original response
                if (response is AuthResponse.Success) {
                    fetchCurrentUserProfile()
                }
            }
        }
    }

    // When signing up and creating profile directly, user data is fresh
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
                    // After direct sign up, we can construct a temporary User object or re-fetch
                    // For simplicity here, we could assume the entered fullName is the one to display immediately
                    // Or call fetchCurrentUserProfile() to get the definitive data from DB
                    fetchCurrentUserProfile()
                }
            }
        }
    }

    // After Google Sign up, profile is created, then fetch it
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

    fun isUserLoggedIn(): Flow<Boolean> { // Keep this for initial splash screen checks
        return authRepository.isUserLoggedIn()
    }

    fun checkIfUserProfileExists(): Flow<Boolean> {
        return authRepository.checkIfUserExists()
    }
}