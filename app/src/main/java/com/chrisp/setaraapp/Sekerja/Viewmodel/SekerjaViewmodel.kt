package com.chrisp.setaraapp.Sekerja.Viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrisp.setaraapp.Model.DataAuth.Repository.Course
import com.chrisp.setaraapp.Model.DataAuth.Repository.Module
import kotlinx.coroutines.launch
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.status.SessionSource.Storage
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from

// Inject constructor


class CourseViewModel : ViewModel() {

    var courses: List<Course> by mutableStateOf<List<Course>>(emptyList())
        private set

    var expandedCourseIds by mutableStateOf(setOf<String>())

    init {
        loadCourses()
    }

    private fun loadCourses() {
        viewModelScope.launch {
            val supabase = createSupabaseClient(
                supabaseUrl = "https://kvpupfnfondfwnguhjug.supabase.co",
                supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imt2cHVwZm5mb25kZnduZ3VoanVnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDA5ODAyMTgsImV4cCI6MjA1NjU1NjIxOH0.ijWBN0onfpzt16PjfIqR6javyLRSkpXukS7yIhAk_k4"
            ) {
                install(Postgrest)
                install(Auth)
            }

            // fetch data, contoh dummy dulu karena decodeList belum ready
            courses = listOf(
                Course(
                    id = "1",
                    title = "Bootcamp Android",
                    description = "Belajar Jetpack Compose dari nol",
                    modules = listOf(
                        Module(
                            id = "1",
                            title = "Programming Fundamental",
                            sesi = 14,
                            content = listOf("Intro to programming", "Git", "OOP", "Exam")
                        )
                    )
                )
            )
        }
    }

    fun toggleCourseExpansion(courseId: String) {
        expandedCourseIds = if (courseId in expandedCourseIds) {
            expandedCourseIds - courseId
        } else {
            expandedCourseIds + courseId
        }
    }
}
