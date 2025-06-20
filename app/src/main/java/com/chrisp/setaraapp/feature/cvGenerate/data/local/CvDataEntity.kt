package com.chrisp.setaraapp.feature.cvGenerate.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cv_data")
data class CvDataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Personal Data
    val fullName: String = "",
    val phone: String = "",
    val email: String = "",
    val linkedin: String = "",
    val address: String = "",
    val summary: String = "",

    // Education Data
    val university: String = "",
    val major: String = "",
    val gpa: String = "",
    val educationStartDate: String = "",
    val educationEndDate: String = "",
    val educationDescription: String = "",

    // Work Experience Data
    val position: String = "",
    val company: String = "",
    val workStartDate: String = "",
    val workEndDate: String = "",
    val workDescription: String = ""
)