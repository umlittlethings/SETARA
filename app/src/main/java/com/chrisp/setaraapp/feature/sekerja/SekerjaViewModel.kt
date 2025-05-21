package com.chrisp.setaraapp.feature.sekerja

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrisp.setaraapp.feature.auth.AuthViewModel
import com.chrisp.setaraapp.feature.sekerja.model.CourseEnrollment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SekerjaViewModel(
    private val authViewModel: AuthViewModel,
    private val sekerjaRepository: SekerjaRepository = SekerjaRepositoryImpl()
) : ViewModel() {

    private val tag = "SekerjaViewModel"

    // State untuk daftar kursus yang diikuti pengguna
    private val _enrolledCourses = MutableStateFlow<List<CourseEnrollment>>(emptyList())
    val enrolledCourses: StateFlow<List<CourseEnrollment>> = _enrolledCourses.asStateFlow()

    // State untuk UI (loading, error)
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    init {
        // Amati perubahan currentUser dari AuthViewModel
        // dan muat pendaftaran saat pengguna tersedia
        viewModelScope.launch {
            authViewModel.currentUser.collect { user ->
                if (user != null) {
                    Log.d(tag, "Current user updated: ${user.id}. Fetching enrollments.")
                    fetchUserEnrollments(user.id)
                } else {
                    Log.d(tag, "User logged out or not available. Clearing enrollments.")
                    _enrolledCourses.value = emptyList() // Bersihkan data jika pengguna logout
                }
            }
        }
    }

    fun fetchUserEnrollments(userId: String) {
        if (userId.isBlank()) {
            Log.w(tag, "fetchUserEnrollments called with blank userId.")
            _errorMessage.value = "User ID tidak valid."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            Log.d(tag, "Attempting to fetch enrollments for user: $userId")

            sekerjaRepository.getUserEnrollments(userId).collect { result ->
                result.fold(
                    onSuccess = { enrollments ->
                        _enrolledCourses.value = enrollments
                        _isLoading.value = false
                        Log.i(tag, "Successfully fetched ${enrollments.size} enrollments for user $userId.")
                    },
                    onFailure = { exception ->
                        _enrolledCourses.value = emptyList()
                        _isLoading.value = false
                        _errorMessage.value = "Gagal memuat program terdaftar: ${exception.message}"
                        Log.e(tag, "Error fetching enrollments for user $userId:", exception)
                    }
                )
            }
        }
    }

    fun refreshEnrollments() {
        viewModelScope.launch {
            val currentUserId = authViewModel.currentUser.value?.id
            if (currentUserId != null) {
                fetchUserEnrollments(currentUserId)
            } else {
                Log.w(tag, "Cannot refresh enrollments, user not logged in.")
            }
        }
    }

     fun getOngoingCourses(): List<CourseEnrollment> {
         return _enrolledCourses.value.filter { !it.completed && it.progress > 0f }
     }

     fun getCompletedCourses(): List<CourseEnrollment> {
         return _enrolledCourses.value.filter { it.completed }
     }
}