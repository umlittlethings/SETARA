package com.chrisp.setaraapp.Sekerja.Viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrisp.setaraapp.Model.DataAuth.Repository.Course
import com.chrisp.setaraapp.Model.DataAuth.Repository.Module
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import com.chrisp.setaraapp.Model.DataAuth.Repository.CourseModule

class CourseViewModel : ViewModel() {
    private val supabase = createSupabaseClient(
        supabaseUrl = "https://kvpupfnfondfwnguhjug.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imt2cHVwZm5mb25kZnduZ3VoanVnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDA5ODAyMTgsImV4cCI6MjA1NjU1NjIxOH0.ijWBN0onfpzt16PjfIqR6javyLRSkpXukS7yIhAk_k4"
    ) {
        install(Postgrest)
    }

    var courses by mutableStateOf(emptyList<Course>())
        private set

    val expandedCourseIds = mutableStateListOf<String>()

    init {
        loadCourses()
    }

    private fun loadCourses() {
        viewModelScope.launch {
            try {
                val courseData = supabase.postgrest["course"].select().decodeList<Course>()
                val moduleData = supabase.postgrest["module"].select().decodeList<Module>()
                val courseModuleData =
                    supabase.postgrest["course_module"].select().decodeList<CourseModule>()

                Log.d("CourseViewModel", "Fetched courses: $courseData")
                Log.d("CourseViewModel", "Fetched modules: $moduleData")
                Log.d("CourseViewModel", "Fetched course_module: $courseModuleData")

                courses = courseData.map { course ->
                    val relatedModuleIds = courseModuleData
                        .filter { it.courseId == course.course_id }
                        .sortedBy { it.orderNum }
                        .map { it.moduleId }

                    val relatedModules = relatedModuleIds.mapNotNull { moduleId ->
                        moduleData.find { it.module_id == moduleId }
                    }

                    course.copy(modules = relatedModules)
                }

            } catch (e: Exception) {
                Log.e("CourseViewModel", "Error loading courses", e)
                courses = emptyList()
            }
        }
    }

    fun toggleCourseExpansion(courseId: String) {
        if (expandedCourseIds.contains(courseId)) {
            expandedCourseIds.remove(courseId)
        } else {
            expandedCourseIds.add(courseId)
        }
    }
}



