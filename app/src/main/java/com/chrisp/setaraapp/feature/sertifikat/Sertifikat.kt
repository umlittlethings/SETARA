package com.chrisp.setaraapp.feature.sertifikat

import com.chrisp.setaraapp.feature.auth.User

data class Sertifikat(
    val courseId: String,
    val courseTitle: String,
    val courseCompany: String,
    val enrollmentDate: String,
    val user: User
)