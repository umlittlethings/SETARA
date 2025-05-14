package com.chrisp.setaraapp.feature.cvGenerate.presentation

import androidx.lifecycle.ViewModel
import com.chrisp.setaraapp.feature.cvGenerate.data.CvFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FormViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CvFormState())
    val uiState: StateFlow<CvFormState> = _uiState.asStateFlow()

    // Personal Data Update Methods
    fun updateFullName(fullName: String) {
        _uiState.update { currentState ->
            currentState.copy(
                personalData = currentState.personalData.copy(fullName = fullName)
            )
        }
    }

    fun updateEmail(email: String) {
        _uiState.update { currentState ->
            currentState.copy(
                personalData = currentState.personalData.copy(email = email)
            )
        }
    }

    fun updateLinkedin(linkedin: String) {
        _uiState.update { currentState ->
            currentState.copy(
                personalData = currentState.personalData.copy(linkedin = linkedin)
            )
        }
    }

    fun updatePhone(phone: String) {
        _uiState.update { currentState ->
            currentState.copy(
                personalData = currentState.personalData.copy(phone = phone)
            )
        }
    }

    fun updateAddress(address: String) {
        _uiState.update { currentState ->
            currentState.copy(
                personalData = currentState.personalData.copy(address = address)
            )
        }
    }

    fun updateSummary(summary: String) {
        _uiState.update { currentState ->
            currentState.copy(
                personalData = currentState.personalData.copy(summary = summary)
            )
        }
    }

    // Education Update Methods
    fun updateUniversity(university: String) {
        _uiState.update { currentState ->
            currentState.copy(
                educationData = currentState.educationData.copy(university = university)
            )
        }
    }

    fun updateMajor(major: String) {
        _uiState.update { currentState ->
            currentState.copy(
                educationData = currentState.educationData.copy(major = major)
            )
        }
    }

    fun updateGPA(gpa: String) {
        _uiState.update { currentState ->
            currentState.copy(
                educationData = currentState.educationData.copy(gpa = gpa)
            )
        }
    }

    fun updateMasukKuliah(startDate: String) {
        _uiState.update { currentState ->
            currentState.copy(
                educationData = currentState.educationData.copy(startDate = startDate)
            )
        }
    }

    fun updateLulusKuliah(endDate: String) {
        _uiState.update { currentState ->
            currentState.copy(
                educationData = currentState.educationData.copy(endDate = endDate)
            )
        }
    }

    fun updateEducationDescription(description: String) {
        _uiState.update { currentState ->
            currentState.copy(
                educationData = currentState.educationData.copy(description = description)
            )
        }
    }

    // Work Experience Update Methods
    fun updatePosition(position: String) {
        _uiState.update { currentState ->
            currentState.copy(
                workExperienceData = currentState.workExperienceData.copy(position = position)
            )
        }
    }

    fun updateCompany(company: String) {
        _uiState.update { currentState ->
            currentState.copy(
                workExperienceData = currentState.workExperienceData.copy(company = company)
            )
        }
    }

    fun updateStartDate(startDate: String) {
        _uiState.update { currentState ->
            currentState.copy(
                workExperienceData = currentState.workExperienceData.copy(startDate = startDate)
            )
        }
    }

    fun updateEndDate(endDate: String) {
        _uiState.update { currentState ->
            currentState.copy(
                workExperienceData = currentState.workExperienceData.copy(endDate = endDate)
            )
        }
    }

    fun updateWorkDescription(description: String) {
        _uiState.update { currentState ->
            currentState.copy(
                workExperienceData = currentState.workExperienceData.copy(description = description)
            )
        }
    }

    // Navigation Methods
    fun navigateToNextPage() {
        if (_uiState.value.currentPage < 2) {
            _uiState.update { currentState ->
                currentState.copy(currentPage = currentState.currentPage + 1)
            }
        }
    }

    fun navigateToPreviousPage() {
        if (_uiState.value.currentPage > 0) {
            _uiState.update { currentState ->
                currentState.copy(currentPage = currentState.currentPage - 1)
            }
        }
    }

    // Form Validation
    fun validatePersonalData(): Boolean {
        val personalData = _uiState.value.personalData
        return personalData.fullName.isNotBlank() &&
                personalData.email.isNotBlank() &&
                personalData.linkedin.isNotBlank() &&
                personalData.phone.isNotBlank() &&
                personalData.summary.isNotBlank() &&
                personalData.address.isNotBlank()
    }

    fun validateEducation(): Boolean {
        val educationData = _uiState.value.educationData
        return educationData.university.isNotBlank() &&
                educationData.major.isNotBlank()
                educationData.gpa.isNotBlank()
                educationData.startDate.isNotBlank()
                educationData.endDate.isNotBlank()
                educationData.description.isNotBlank()
    }

    fun validateWorkExperience(): Boolean {
        val workExperienceData = _uiState.value.workExperienceData
        return workExperienceData.position.isNotBlank() &&
                workExperienceData.company.isNotBlank() &&
                workExperienceData.startDate.isNotBlank()
    }

    fun generateHtmlContent(): String {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <title>Resume - ${_uiState.value.personalData.fullName}</title>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; margin: 40px; }
                    .header { text-align: center; margin-bottom: 30px; }
                    .section { margin-bottom: 25px; }
                    h2 { color: #2c3e50; border-bottom: 2px solid #3498db; padding-bottom: 5px; }
                    .date-range { float: right; color: #7f8c8d; }
                    ul { margin-top: 5px; padding-left: 20px; }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>${_uiState.value.personalData.fullName}</h1>
                    <p>${_uiState.value.personalData.phone} | <a href="${_uiState.value.personalData.email}">${_uiState.value.personalData.email}</a> | <a href="${_uiState.value.personalData.linkedin}">LinkedIn</a> | ${_uiState.value.personalData.address}</p>
                </div>
            
                <div class="section">
                    <h2>SUMMARY</h2>
                    <p>${_uiState.value.personalData.summary}</p>
                </div>
            
                <div class="section">
                    <h2>EDUCATION</h2>
                    <p>
                        <span class="date-range">${_uiState.value.educationData.startDate} - ${_uiState.value.educationData.endDate}</span>
                        <strong>${_uiState.value.educationData.university}</strong><br>
                        Sarjana ${_uiState.value.educationData.major}, ${_uiState.value.educationData.gpa}/4.00<br>
                    </p>
                    <ul>
                        <li>${_uiState.value.educationData.description}</li>
                    </ul>
                </div>
            
                <div class="section">
                    <h2>EXPERIENCE</h2>
                    <p>
                        <span class="date-range">${_uiState.value.workExperienceData.startDate} - ${_uiState.value.workExperienceData.endDate}</span>
                        <strong>${_uiState.value.workExperienceData.company}</strong><br>
                        ${_uiState.value.workExperienceData.position}<br>
                    </p>
                    <ul>
                        <li>${_uiState.value.workExperienceData.description}</li>
                    </ul>
                </div>
            
                <div class="section">
                    <h2>INFORMASI TAMBAHAN</h2>
                    <ul>
                        <li><strong>Hard Skill: </strong> blablaba;</li>
                        <li><strong>Tool: </strong> blablaba;</li>
                        <li><strong>Bahasa: </strong> blablaba;</li>
                        <li><strong>Sertifikat: </strong> blablaba;</li>
                    </ul>
                </div>
            </body>
            </html>
        """.trimIndent()
    }
}
