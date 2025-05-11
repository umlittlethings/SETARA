package com.chrisp.setaraapp.feature.auth

import androidx.compose.foundation.background
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chrisp.setaraapp.component.TextFieldUI
import kotlinx.coroutines.launch
import com.chrisp.setaraapp.R

@Composable
fun CompleteProfileScreen(
    viewModel: AuthViewModel = viewModel(),
    onComplete: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var categoryDisability by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Header
            Text(
                text = "Lengkapi Profil",
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            Text(
                text = "Isi data berikut untuk melengkapi profil Anda",
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Full Name Field
            TextFieldUI(
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = "Masukkan Nama Lengkap",
                leadingIcon = Icons.Default.Person,
                keyboardType = KeyboardType.Text
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Birth Date Field
            TextFieldUI(
                value = birthDate,
                onValueChange = { birthDate = it },
                placeholder = "YYYY-MM-DD",
                leadingIcon = Icons.Default.DateRange,
                keyboardType = KeyboardType.Text
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Disability Category Field
            TextFieldUI(
                value = categoryDisability,
                onValueChange = { categoryDisability = it },
                placeholder = "Masukkan Kategori Disabilitas",
                leadingIcon = Icons.Default.Person,
                keyboardType = KeyboardType.Text
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Phone Number Field
            TextFieldUI(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                placeholder = "Masukkan Nomor Telepon",
                leadingIcon = Icons.Default.Phone,
                keyboardType = KeyboardType.Phone
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Address Field
            TextFieldUI(
                value = address,
                onValueChange = { address = it },
                placeholder = "Masukkan Alamat",
                leadingIcon = Icons.Default.LocationOn,
                keyboardType = KeyboardType.Text
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Error Message
            if (errorMessage != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Submit Button
            Button(
                onClick = {
                    if (
                        fullName.isNotBlank() && birthDate.isNotBlank() && categoryDisability.isNotBlank() &&
                        phoneNumber.isNotBlank() && address.isNotBlank()
                    ) {
                        isLoading = true
                        errorMessage = null

                        coroutineScope.launch {
                            viewModel.signUpWithGoogleAndCreateProfile(
                                fullName = fullName,
                                birthDate = birthDate,
                                categoryDisability = categoryDisability,
                                phoneNumber = phoneNumber,
                                address = address
                            ).collect { response ->
                                isLoading = false
                                when (response) {
                                    is AuthResponse.Success -> onComplete()
                                    is AuthResponse.Error -> errorMessage = response.message ?: "Gagal menyimpan profil"
                                }
                            }
                        }
                    } else {
                        errorMessage = "Silakan isi semua kolom"
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.magenta_80)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Simpan",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(vertical = 8.dp),
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}