package com.chrisp.setaraapp.feature.sertifikat

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
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
            <title>Sertifikat Kelulusan - ${sertifikat.courseTitle}</title>
            <style>
                body {
                    font-family: 'Helvetica', 'Arial', sans-serif;
                    text-align: center;
                    border: 10px solid #980053;
                    padding: 40px;
                    height: 90%;
                    box-sizing: border-box;
                }
                .container {
                    border: 2px solid #D1006F;
                    padding: 30px;
                    height: 100%;
                    position: relative;
                }
                h1 {
                    font-size: 48px;
                    color: #980053;
                    margin-bottom: 20px;
                }
                .subtitle {
                    font-size: 24px;
                    color: #333;
                    margin-bottom: 40px;
                }
                .presented-to {
                    font-size: 18px;
                    margin-bottom: 10px;
                }
                .name {
                    font-size: 36px;
                    font-weight: bold;
                    color: #FF5C00;
                    margin-bottom: 20px;
                    border-bottom: 2px solid #EBEBEB;
                    display: inline-block;
                    padding-bottom: 10px;
                }
                .description {
                    font-size: 18px;
                    margin-bottom: 50px;
                }
                .footer {
                    position: absolute;
                    bottom: 40px;
                    width: 100%;
                    left: 0;
                    display: flex;
                    justify-content: space-around;
                    align-items: flex-end;
                }
                .signature {
                    border-top: 1px solid #333;
                    padding-top: 10px;
                    width: 200px;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <h1>SERTIFIKAT KELULUSAN</h1>
                <p class="subtitle">Diberikan kepada:</p>
                <h2 class="name">${sertifikat.user.f_name}</h2>
                <p class="description">
                    Atas keberhasilannya telah menyelesaikan program<br>
                    <strong>${sertifikat.courseTitle}</strong><br>
                    diselenggarakan oleh <strong>SETARA</strong> bekerja sama dengan <strong>${sertifikat.courseCompany}</strong>.
                </p>
                <div class="footer">
                    <div>
                        <p><strong>CEO SETARA</strong></p>
                        <div class="signature"></div>
                        <p>(Nama CEO)</p>
                    </div>
                    <div>
                        <p>Malang, ${currentDate}</p>
                    </div>
                    <div>
                        <p><strong>CEO ${sertifikat.courseCompany}</strong></p>
                        <div class="signature"></div>
                        <p>(Nama CEO Mitra)</p>
                    </div>
                </div>
            </div>
        </body>
        </html>
        """.trimIndent()
    }
}