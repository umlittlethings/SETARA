package com.chrisp.setaraapp.feature.sekerja.detailTugas

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrisp.setaraapp.feature.sekerja.SekerjaRepository
import com.chrisp.setaraapp.feature.sekerja.SekerjaRepositoryImpl // Assuming you might inject or default it
import com.chrisp.setaraapp.feature.sekerja.detailTugas.model.Assignment
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailTugasViewModel(
    private val sekerjaRepository: SekerjaRepository = SekerjaRepositoryImpl()
) : ViewModel() {

    private val _assignment = mutableStateOf<Assignment?>(null)
    val assignment: State<Assignment?> = _assignment

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage


    private val _isSubmitting = mutableStateOf(false)
    val isSubmitting: State<Boolean> = _isSubmitting

    private val _submitResult = mutableStateOf<Result<Unit>?>(null)
    val submitResult: State<Result<Unit>?> = _submitResult




    fun fetchAssignmentDetails(courseId: String, assignmentId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _assignment.value = null

            sekerjaRepository.getAssignmentsForCourse(courseId)
                .catch { e ->
                    _errorMessage.value = "Failed to load assignment: ${e.message}"
                    _isLoading.value = false
                }
                .collect { result ->
                    result.fold(
                        onSuccess = { assignments ->
                            val foundAssignment = assignments.find { it.assignmentId == assignmentId }
                            if (foundAssignment != null) {
                                _assignment.value = foundAssignment
                            } else {
                                _errorMessage.value = "Assignment with ID $assignmentId not found in course $courseId."
                            }
                            _isLoading.value = false
                        },
                        onFailure = { e ->
                            _errorMessage.value = "Error fetching assignments: ${e.message}"
                            _isLoading.value = false
                        }
                    )
                }
        }
    }


}