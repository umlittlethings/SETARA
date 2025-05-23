package com.chrisp.setaraapp.feature.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrisp.setaraapp.feature.sekerja.model.Course
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
            repository.getAllCoursesWithModules().fold(
                onSuccess = { fetchedCourses ->
                    courses = fetchedCourses
                    Log.d("HomeViewModel", "Successfully loaded ${fetchedCourses.size} courses.")
                    isLoading = false
                },
                onFailure = { exception ->
                    Log.e("HomeViewModel", "Error loading courses", exception)
                    errorMessage = "Gagal memuat program. Silakan coba lagi."
                    courses = emptyList()
                    isLoading = false
                }
            )
        }
    }
}