package com.chrisp.setaraapp.feature.sertifikat

import com.chrisp.setaraapp.feature.auth.User

// Model data gabungan untuk UI Sertifikat
data class Sertifikat(
    val courseId: String,
    val courseTitle: String,
    val courseCompany: String,
    val enrollmentDate: String, // Tanggal pendaftaran atau penyelesaian
    val user: User // Data pengguna untuk ditampilkan di sertifikat
)