package com.chrisp.setaraapp.feature.sekerja

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrisp.setaraapp.feature.auth.AuthViewModel
import com.chrisp.setaraapp.feature.sekerja.detailTugas.model.Assignment
import com.chrisp.setaraapp.feature.sekerja.model.CourseEnrollment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class SekerjaViewModel(
    private val authViewModel: AuthViewModel,
    private val sekerjaRepository: SekerjaRepository = SekerjaRepositoryImpl()
) : ViewModel() {

    private val tag = "SekerjaViewModel"

    private val _enrolledCourses = MutableStateFlow<List<CourseEnrollment>>(emptyList())
    val enrolledCourses: StateFlow<List<CourseEnrollment>> = _enrolledCourses.asStateFlow()

    private val _assignments = MutableStateFlow<List<Assignment>>(emptyList())
    val assignments: StateFlow<List<Assignment>> = _assignments.asStateFlow()

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    init {
        viewModelScope.launch {
            authViewModel.currentUser.collect { user ->
                if (user != null) {
                    fetchUserEnrollments(user.id)
                } else {
                    _enrolledCourses.value = emptyList()
                }
            }
        }
    }

    fun generateMissingSubmissions(userId: String) {
        viewModelScope.launch {
            sekerjaRepository.generateMissingSubmissionsForUser(userId).collect { result ->
                result.onFailure {
                    Log.e("SubmissionGenerator", "Failed: ${it.message}")
                }
            }
        }
    }

    fun joinCourse(courseId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val user = authViewModel.currentUser.value
            if (user != null) {
                val userId = user.id
                val joinResult = sekerjaRepository.joinCourse(userId, courseId)
                if (joinResult.isSuccess) {
                    val submissionResult = sekerjaRepository.createInitialSubmissions(UUID.fromString(userId), courseId)
                    if (submissionResult.isFailure) {
                        _errorMessage.value = submissionResult.exceptionOrNull()?.message
                    }
                    fetchUserEnrollments(userId) // Refresh data
                } else {
                    _errorMessage.value = joinResult.exceptionOrNull()?.message
                }
            }
            _isLoading.value = false
        }
    }

    fun fetchUserEnrollments(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            sekerjaRepository.getUserEnrollments(userId).collect { result ->
                result.fold(
                    onSuccess = {
                        Log.d(tag, "Enrollments fetched: $it")
                        _enrolledCourses.value = it
                        fetchAssignmentsForUser(userId)
                    },
                    onFailure = {
                        Log.e(tag, "Failed fetch enrollments: ${it.message}")
                        _errorMessage.value = it.message
                    }
                )
                _isLoading.value = false
            }
        }
    }

    fun fetchAssignmentsForUser(userId: String) {
        viewModelScope.launch {
            val courses = _enrolledCourses.value
            Log.d(tag, "Fetching assignments for courses: $courses")
            val allAssignments = mutableListOf<Assignment>()

            for (course in courses) {
                sekerjaRepository.getAssignmentsForCourse(course.courseId).collect { result ->
                    result.fold(
                        onSuccess = {
                            Log.d(tag, "Assignments for course ${course.courseId}: $it")
                            allAssignments.addAll(it)
                        },
                        onFailure = {
                            Log.e(tag, "Failed fetch assignments: ${it.message}")
                            _errorMessage.value = it.message
                        }
                    )
                }
            }

            _assignments.value = allAssignments
            Log.d(tag, "All assignments combined: $allAssignments")
        }
    }



    fun refreshEnrollments() {
        viewModelScope.launch {
            authViewModel.currentUser.value?.id?.let {
                fetchUserEnrollments(it)
            }
        }
    }

    fun getOngoingCourses(): List<CourseEnrollment> =
        _enrolledCourses.value.filter { !it.completed && it.progress > 0f }

    fun getCompletedCourses(): List<CourseEnrollment> =
        _enrolledCourses.value.filter { it.completed }
}
