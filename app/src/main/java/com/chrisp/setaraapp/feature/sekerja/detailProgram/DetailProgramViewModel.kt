package com.chrisp.setaraapp.feature.sekerja.detailProgram

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrisp.setaraapp.feature.repository.SupabaseInstance
import com.chrisp.setaraapp.feature.sekerja.SekerjaRepository
import com.chrisp.setaraapp.feature.sekerja.SekerjaRepositoryImpl
import com.chrisp.setaraapp.feature.sekerja.model.CourseEnrollment
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch // Import catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map // Import map
import kotlinx.coroutines.launch

class DetailProgramViewModel(
    private val sekerjaRepository: SekerjaRepository = SekerjaRepositoryImpl()
): ViewModel() {
    private val supabase = SupabaseInstance.client

    private val currentUserId: String? get() = supabase.auth.currentUserOrNull()?.id
    val daftarCourses = mutableStateListOf<String>()

    private val _enrollmentStatus = MutableStateFlow<EnrollmentStatus>(EnrollmentStatus.Idle)
    val enrollmentStatus: StateFlow<EnrollmentStatus> = _enrollmentStatus.asStateFlow()

    fun enrollToCourse(courseId: String) {
        val userId = currentUserId
        if (userId == null) {
            Log.e("DetailProgramViewModel", "User not logged in")
            _enrollmentStatus.value = EnrollmentStatus.Error("Pengguna belum login.")
            return
        }

        viewModelScope.launch {
            _enrollmentStatus.value = EnrollmentStatus.Loading
            // Bungkus hasil dari isUserEnrolled dengan Result menggunakan .map dan .catch
            val isEnrolledResult: Result<Boolean> = sekerjaRepository.isUserEnrolled(userId, courseId)
                .map { Result.success(it) } // Bungkus hasil sukses
                .catch { e -> emit(Result.failure(e)) } // Tangkap error dan bungkus
                .first() // Ambil item pertama (yang sekarang adalah Result)

            isEnrolledResult.fold(
                onSuccess = { isEnrolled ->
                    if (isEnrolled) {
                        _enrollmentStatus.value = EnrollmentStatus.AlreadyEnrolled("Anda telah terdaftar di program course ini.")
                        Log.d("DetailProgramViewModel", "User $userId already enrolled in course $courseId")
                    } else {
                        try {
                            val newEnrollment = CourseEnrollment(
                                userId = userId,
                                courseId = courseId,
                                completed = false,
                                progress = 0f,
                                lastOpened = null
                            )

                            supabase.postgrest["daftar"].insert(newEnrollment)
                            daftarCourses.add(courseId)
                            _enrollmentStatus.value = EnrollmentStatus.Success
                            Log.d("DetailProgramViewModel", "Enrolled user $userId to course $courseId")
                        } catch (e: Exception) {
                            Log.e("DetailProgramViewModel", "Failed to enroll to course", e)
                            _enrollmentStatus.value = EnrollmentStatus.Error("Gagal mendaftar ke kursus: ${e.message}")
                        }
                    }
                },
                onFailure = { exception ->
                    Log.e("DetailProgramViewModel", "Failed to check enrollment status from repository", exception)
                    _enrollmentStatus.value = EnrollmentStatus.Error("Gagal memeriksa status pendaftaran: ${exception.message}")
                }
            )
        }
    }

    fun resetEnrollmentStatus() {
        _enrollmentStatus.value = EnrollmentStatus.Idle
    }
}

sealed class EnrollmentStatus {
    object Idle : EnrollmentStatus()
    object Loading : EnrollmentStatus()
    object Success : EnrollmentStatus()
    data class AlreadyEnrolled(val message: String) : EnrollmentStatus()
    data class Error(val message: String) : EnrollmentStatus()
}