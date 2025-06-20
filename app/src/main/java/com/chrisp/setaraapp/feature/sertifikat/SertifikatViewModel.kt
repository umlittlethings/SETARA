package com.chrisp.setaraapp.feature.sertifikat

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrisp.setaraapp.feature.auth.AuthViewModel
import com.chrisp.setaraapp.feature.cvGenerate.domain.PdfGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class SertifikatViewModel(private val authViewModel: AuthViewModel) : ViewModel() {

    private val repository = SertifikatRepository()

    private val _certificates = MutableStateFlow<List<Sertifikat>>(emptyList())
    val certificates: StateFlow<List<Sertifikat>> = _certificates.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        fetchCertificates()
    }

    fun fetchCertificates() {
        viewModelScope.launch {
            _isLoading.value = true
            val currentUser = authViewModel.currentUser.value
            val userId = currentUser?.id

            if (userId == null) {
                _errorMessage.value = "Pengguna tidak ditemukan."
                _isLoading.value = false
                return@launch
            }

            val result = repository.getCompletedCourses(userId)
            result.fold(
                onSuccess = { coursePairs ->
                    _certificates.value = coursePairs.map { (course, enrollment) ->
                        Sertifikat(
                            courseId = course.courseId,
                            courseTitle = course.title,
                            courseCompany = course.company,
                            enrollmentDate = enrollment.lastOpened ?: "Tanggal tidak tersedia",
                            user = currentUser
                        )
                    }
                },
                onFailure = {
                    _errorMessage.value = "Gagal memuat sertifikat: ${it.message}"
                }
            )
            _isLoading.value = false
        }
    }

    fun downloadCertificate(context: Context, sertifikat: Sertifikat) {
        viewModelScope.launch {
            val htmlContent = generateCertificateHtml(sertifikat)
            val success = PdfGenerator.generatePdf(context, htmlContent)
            if (success) {
                Toast.makeText(context, "Sertifikat berhasil diunduh!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Gagal mengunduh sertifikat.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun generateCertificateHtml(sertifikat: Sertifikat): String {
        val currentDate = java.text.SimpleDateFormat("d MMMM yyyy", Locale("id", "ID")).format(java.util.Date())
        return """
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CV - [Nama Anda]</title>
    <style>
        body {
            font-family: 'Arial', sans-serif; /* Font yang lebih umum dan mudah dibaca untuk CV */
            line-height: 1.6;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4; /* Warna latar belakang netral */
            color: #333;
        }

        .cv-container {
            width: 80%; /* Atau ukuran spesifik seperti 21cm untuk A4 */
            max-width: 800px;
            margin: 30px auto;
            background-color: #fff;
            padding: 40px;
            border-radius: 8px; /* Sedikit lengkungan untuk tampilan modern */
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
        }

        .header {
            text-align: center;
            margin-bottom: 40px;
            border-bottom: 2px solid #980053; /* Mengambil warna aksen dari sertifikat */
            padding-bottom: 20px;
        }

        .header h1 {
            font-size: 36px;
            color: #333; /* Warna judul utama lebih netral */
            margin-bottom: 5px;
            font-weight: bold;
        }

        .header .contact-info {
            font-size: 14px;
            color: #555;
        }

        .header .contact-info a {
            color: #980053; /* Warna link konsisten dengan aksen */
            text-decoration: none;
        }

        .header .contact-info a:hover {
            text-decoration: underline;
        }

        .section {
            margin-bottom: 30px;
        }

        .section h2 {
            font-size: 22px;
            color: #980053; /* Warna aksen untuk judul bagian */
            border-bottom: 1px solid #eee;
            padding-bottom: 8px;
            margin-bottom: 15px;
        }

        .section .item {
            margin-bottom: 15px;
        }

        .section .item h3 {
            font-size: 18px;
            color: #444;
            margin-bottom: 5px;
            font-weight: 600;
        }

        .section .item .date {
            font-size: 14px;
            color: #777;
            font-style: italic;
            margin-bottom: 5px;
        }

        .section .item p, .section .item ul {
            font-size: 15px;
            color: #555;
            margin-bottom: 5px;
        }

        .section .item ul {
            list-style-type: disc; /* Atau 'circle' atau 'square' */
            padding-left: 20px;
        }

        .skills ul {
            list-style-type: none;
            padding: 0;
        }

        .skills li {
            background-color: #e9e9e9;
            color: #333;
            display: inline-block;
            padding: 5px 12px;
            margin: 5px 5px 5px 0;
            border-radius: 4px;
            font-size: 14px;
        }

        /* Opsi Tata Letak Dua Kolom (jika diinginkan) */
        .two-column-layout {
            display: flex;
            gap: 30px; /* Jarak antar kolom */
        }

        .main-content {
            flex: 2; /* Kolom utama lebih lebar */
        }

        .sidebar {
            flex: 1; /* Kolom samping lebih sempit */
        }

        @media (max-width: 768px) {
            .cv-container {
                width: 95%;
                padding: 20px;
            }
            .header h1 {
                font-size: 28px;
            }
            .section h2 {
                font-size: 20px;
            }
            .two-column-layout {
                flex-direction: column; /* Ubah jadi satu kolom di layar kecil */
            }
        }

    </style>
</head>
<body>
    <div class="cv-container">
        <div class="header">
            <h1>[Nama Lengkap Anda]</h1>
            <p class="contact-info">
                [Alamat Email Anda] | [Nomor Telepon Anda] | <a href="[Link LinkedIn Anda (jika ada)]" target="_blank">LinkedIn</a> | <a href="[Link Portofolio/GitHub Anda (jika ada)]" target="_blank">Portofolio</a>
            </p>
        </div>

        <!-- Contoh Tata Letak Dua Kolom -->
        <div class="two-column-layout">
            <div class="main-content">
                <div class="section">
                    <h2>Ringkasan Profil</h2>
                    <p>
                        [Tulis ringkasan singkat tentang diri Anda, tujuan karir, dan keahlian utama yang relevan dengan pekerjaan yang dilamar. Buat semenarik mungkin.]
                    </p>
                </div>

                <div class="section">
                    <h2>Pengalaman Kerja</h2>
                    <div class="item">
                        <h3>[Jabatan Terakhir Anda]</h3>
                        <p><strong>[Nama Perusahaan]</strong> | [Kota, Negara]</p>
                        <p class="date">[Bulan Tahun Mulai] – [Bulan Tahun Selesai atau Sekarang]</p>
                        <ul>
                            <li>[Tanggung jawab dan pencapaian spesifik 1]</li>
                            <li>[Tanggung jawab dan pencapaian spesifik 2]</li>
                            <li>[Gunakan action verbs (misalnya: Mengembangkan, Memimpin, Mengelola)]</li>
                        </ul>
                    </div>
                    <div class="item">
                        <h3>[Jabatan Sebelumnya]</h3>
                        <p><strong>[Nama Perusahaan Sebelumnya]</strong> | [Kota, Negara]</p>
                        <p class="date">[Bulan Tahun Mulai] – [Bulan Tahun Selesai]</p>
                        <ul>
                            <li>[Tanggung jawab dan pencapaian spesifik 1]</li>
                            <li>[Tanggung jawab dan pencapaian spesifik 2]</li>
                        </ul>
                    </div>
                    <!-- Tambahkan item pengalaman kerja lainnya jika ada -->
                </div>

                <div class="section">
                    <h2>Pendidikan</h2>
                    <div class="item">
                        <h3>[Gelar Pendidikan Anda]</h3>
                        <p><strong>[Nama Institusi Pendidikan]</strong> | [Kota, Negara]</p>
                        <p class="date">[Tahun Lulus]</p>
                        <p>[Informasi tambahan seperti IPK (jika relevan dan baik), jurusan, atau tesis.]</p>
                    </div>
                    <!-- Tambahkan item pendidikan lainnya jika ada -->
                </div>
            </div>

            <div class="sidebar">
                <div class="section skills">
                    <h2>Keahlian</h2>
                    <ul>
                        <li>[Keahlian Teknis 1]</li>
                        <li>[Keahlian Teknis 2]</li>
                        <li>[Bahasa Pemrograman]</li>
                        <li>[Software/Tools]</li>
                        <li>[Keahlian Non-Teknis (Soft Skills)]</li>
                        <li>[Bahasa (misal: Indonesia - Native, Inggris - Fluent)]</li>
                    </ul>
                </div>

                <
        """.trimIndent()
    }
}