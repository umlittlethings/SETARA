package com.chrisp.setaraapp.feature.cvGenerate.data

data class CvFormState(
    val personalData: PersonalData = PersonalData(),
    val educationData: EducationData = EducationData(),
    val workExperienceData: WorkExperienceData = WorkExperienceData(),
    val currentPage: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)