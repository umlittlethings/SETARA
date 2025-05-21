package com.chrisp.setaraapp.feature.sekerja.detailProgram

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrisp.setaraapp.feature.repository.SupabaseInstance
import com.chrisp.setaraapp.feature.sekerja.model.CourseEnrollment
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class DetailProgramViewModel: ViewModel() {
    private val supabase = SupabaseInstance.client

    private val currentUserId: String? get() = supabase.auth.currentUserOrNull()?.id
    val daftarCourses = mutableStateListOf<String>() // untuk nyimpen course_id yang sudah didaftar user

    fun enrollToCourse(courseId: String) {
        val userId = currentUserId
        if (userId == null) {
            Log.e("CourseViewModel", "User not logged in")
            return
        }

        viewModelScope.launch {
            try {
                val newEnrollment = CourseEnrollment(
                    userId = userId,
                    courseId = courseId,
                    completed = false,
                    progress = 0f,
                    lastOpened = null
                )

                supabase.postgrest["daftar"].insert(newEnrollment) // ✅ benar sekarang
                daftarCourses.add(courseId)

                Log.d("CourseViewModel", "Enrolled to course $courseId")
            } catch (e: Exception) {
                Log.e("CourseViewModel", "Failed to enroll to course", e)
            }
        }
    }
}