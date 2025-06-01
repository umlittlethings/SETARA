package com.chrisp.setaraapp.feature.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrisp.setaraapp.feature.repository.SupabaseInstance
import com.chrisp.setaraapp.feature.sekerja.model.Course
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: HomeRepository = HomeRepositoryImpl()
) : ViewModel() {

    var courses by mutableStateOf<List<Course>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set


    init {
        loadCourses()
    }

    fun loadCourses() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                repository.getAllCoursesWithModules().fold(
                    onSuccess = { fetchedCourses ->
                        val coursesWithFullUrls = fetchedCourses.map { course ->
                            // Get the storage bucket reference
                            val bucket = SupabaseInstance.client.storage.from("imgcompany")

                            // Generate public URL from the image path stored in database
                            val publicUrl = bucket.publicUrl(course.imagePath)

                            Log.d("HomeViewModel_URL", "Course: ${course.title}")
                            Log.d("HomeViewModel_URL", "Original path: ${course.imagePath}")
                            Log.d("HomeViewModel_URL", "Public URL: $publicUrl")

                            // Return course with updated image path
                            course.copy(imagePath = publicUrl)
                        }

                        courses = coursesWithFullUrls
                        isLoading = false
                        Log.d("HomeViewModel", "Successfully loaded ${courses.size} courses")
                    },
                    onFailure = { exception ->
                        Log.e("HomeViewModel", "Error loading courses from repository", exception)
                        errorMessage = "Gagal memuat program. Silakan coba lagi."
                        courses = emptyList()
                        isLoading = false
                    }
                )
            } catch (exception: Exception) {
                Log.e("HomeViewModel", "Unexpected error in loadCourses", exception)
                errorMessage = "Terjadi kesalahan yang tidak terduga. Silakan coba lagi."
                courses = emptyList()
                isLoading = false
            }
        }
    }

    // Helper function to get public URL (can be used elsewhere if needed)
    private fun getPublicImageUrl(imagePath: String): String {
        return try {
            val bucket = SupabaseInstance.client.storage.from("imgcompany")
            bucket.publicUrl(imagePath)
        } catch (e: Exception) {
            Log.e("HomeViewModel", "Error generating public URL for path: $imagePath", e)
            "" // Return empty string as fallback
        }
    }
}