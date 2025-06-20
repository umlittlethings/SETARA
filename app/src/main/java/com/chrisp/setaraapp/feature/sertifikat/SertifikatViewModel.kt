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
import android.util.Log

class SertifikatViewModel(private val authViewModel: AuthViewModel) : ViewModel() {

    private val repository = SertifikatRepository()

    private val _certificates = MutableStateFlow<List<Sertifikat>>(emptyList())
    val certificates: StateFlow<List<Sertifikat>> = _certificates.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun fetchCertificates() {
        viewModelScope.launch {
            _isLoading.value = true
            val currentUser = authViewModel.currentUser.value
            val userId = currentUser?.id

            Log.d("SertifikatVM", "fetchCertificates called. CurrentUser: $currentUser, UserId: $userId") // Logging

            if (userId == null) {
                Log.e("SertifikatVM", "User ID is null. CurrentUser from AuthVM: ${authViewModel.currentUser.value}")
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
                <html lang="id">
                <head>
                    <meta charset="UTF-8">
                    <title>Sertifikat Kelulusan - ${sertifikat.courseTitle}</title>
                    <style>
                        body {
                            background: #f9f6f2;
                            margin: 0;
                            padding: 0;
                            font-family: 'Georgia', 'Times New Roman', serif;
                        }
                        .certificate-border {
                            border: 8px solid #bfa14a;
                            border-radius: 24px;
                            margin: 40px auto;
                            max-width: 900px;
                            background: #fff;
                            box-shadow: 0 8px 32px rgba(80, 0, 60, 0.08);
                            padding: 0;
                        }
                        .certificate-content {
                            padding: 48px 60px 40px 60px;
                            text-align: center;
                            position: relative;
                        }
                        .seal {
                            position: absolute;
                            top: 32px;
                            right: 60px;
                            width: 80px;
                            height: 80px;
                            background: radial-gradient(circle, #bfa14a 60%, #fff 100%);
                            border-radius: 50%;
                            border: 4px solid #980053;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            font-size: 28px;
                            color: #980053;
                            font-weight: bold;
                            letter-spacing: 2px;
                        }
                        h1 {
                            font-size: 40px;
                            color: #980053;
                            margin-bottom: 8px;
                            letter-spacing: 2px;
                            font-weight: bold;
                        }
                        .subtitle {
                            font-size: 20px;
                            color: #bfa14a;
                            margin-bottom: 32px;
                            font-style: italic;
                        }
                        .recipient {
                            font-size: 32px;
                            color: #333;
                            font-weight: bold;
                            margin-bottom: 16px;
                            border-bottom: 2px solid #bfa14a;
                            display: inline-block;
                            padding: 8px 32px 8px 32px;
                            background: #f9f6f2;
                            border-radius: 8px;
                        }
                        .description {
                            font-size: 20px;
                            color: #444;
                            margin-bottom: 40px;
                            line-height: 1.6;
                        }
                        .footer {
                            display: flex;
                            justify-content: space-between;
                            align-items: flex-end;
                            margin-top: 48px;
                        }
                        .footer .sign-block {
                            width: 30%;
                            text-align: center;
                        }
                        .signature {
                            border-top: 1.5px solid #980053;
                            margin: 24px 0 8px 0;
                            height: 32px;
                        }
                        .footer p {
                            margin: 0;
                            color: #333;
                            font-size: 16px;
                        }
                        .date-block {
                            width: 30%;
                            text-align: center;
                            font-size: 16px;
                            color: #bfa14a;
                        }
                        @media (max-width: 700px) {
                            .certificate-content { padding: 24px 8px; }
                            .seal { right: 16px; top: 16px; }
                            .footer { flex-direction: column; gap: 24px; }
                            .footer .sign-block, .date-block { width: 100%; }
                        }
                    </style>
                </head>
                <body>
                    <div class="certificate-border">
                        <div class="certificate-content">
                            <h1>SERTIFIKAT KELULUSAN</h1>
                            <div class="subtitle">Diberikan kepada</div>
                            <div class="recipient">${sertifikat.user.f_name}</div>
                            <div class="description">
                                Atas keberhasilannya telah menyelesaikan program<br>
                                <strong>${sertifikat.courseTitle}</strong><br>
                                yang diselenggarakan oleh <strong>SETARA</strong> bekerja sama dengan <strong>${sertifikat.courseCompany}</strong>.
                            </div>
                            <div class="footer">
                                <div class="sign-block">
                                    <div class="signature"></div>
                                    <p><strong>CEO SETARA</strong></p>
                                    <p>(Nama CEO)</p>
                                </div>
                                <div class="date-block">
                                    Malang, ${currentDate}
                                </div>
                                <div class="sign-block">
                                    <div class="signature"></div>
                                    <p><strong>CEO ${sertifikat.courseCompany}</strong></p>
                                    <p>(Nama CEO Mitra)</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </body>
                </html>
        """.trimIndent()
    }
}