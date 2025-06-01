package com.chrisp.setaraapp.feature.sekerja.detailTugas

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrisp.setaraapp.feature.sekerja.SekerjaRepository
import com.chrisp.setaraapp.feature.sekerja.SekerjaRepositoryImpl
import com.chrisp.setaraapp.feature.sekerja.detailTugas.model.Assignment
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.InputStream
import java.util.UUID

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

    fun submitAssignment(
        context: Context,
        userId: String,
        assignmentId: String,
        fileUri: Uri
    ) {
        viewModelScope.launch {
            _isSubmitting.value = true
            _submitResult.value = null

            try {
                // Get input stream from URI
                val inputStream = context.contentResolver.openInputStream(fileUri)
                if (inputStream == null) {
                    _submitResult.value = Result.failure(Exception("Could not open file"))
                    _isSubmitting.value = false
                    return@launch
                }

                // Upload file and update submission
                sekerjaRepository.submitAssignmentWithFile(
                    userId = userId,
                    assignmentId = assignmentId,
                    fileInputStream = inputStream,
                    fileName = getFileName(context, fileUri)
                ).catch { e ->
                    _submitResult.value = Result.failure(e)
                    _isSubmitting.value = false
                }.collect { result ->
                    _submitResult.value = result
                    _isSubmitting.value = false
                }

            } catch (e: Exception) {
                _submitResult.value = Result.failure(e)
                _isSubmitting.value = false
            }
        }
    }

    private fun getFileName(context: Context, uri: Uri): String {
        return try {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                val nameIndex = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                if (it.moveToFirst() && nameIndex >= 0) {
                    it.getString(nameIndex) ?: "submission_${UUID.randomUUID()}"
                } else {
                    "submission_${UUID.randomUUID()}"
                }
            } ?: "submission_${UUID.randomUUID()}"
        } catch (e: Exception) {
            "submission_${UUID.randomUUID()}"
        }
    }
}