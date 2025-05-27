package com.chrisp.setaraapp.feature.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon // Import Icon dari M3
import androidx.compose.material3.IconButton // Import IconButton dari M3
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation // Import VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chrisp.setaraapp.R
import com.chrisp.setaraapp.component.TextFieldUI
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = viewModel(),
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    onNavigateToCompleteProfile: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var categoryDisability by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) } // State untuk visibilitas password
    var confirmPasswordVisible by remember { mutableStateOf(false) } // State untuk visibilitas confirm password

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

            Image(
                painter = painterResource(id = R.drawable.ic_setara),
                contentDescription = "Setara Logo",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Buat Akun",
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
            )
            Text(
                text = "Buat akun agar kamu bisa mengakses semua fitur di aplikasi ini",
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            // --- Form Fields ---
            Text("Nama Lengkap", style = MaterialTheme.typography.labelLarge, modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp))
            TextFieldUI(
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = "Masukkan Nama Lengkap",
                leadingIcon = Icons.Default.Person,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Email", style = MaterialTheme.typography.labelLarge, modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp))
            TextFieldUI(
                value = email,
                onValueChange = { email = it },
                placeholder = "setara@mail.com",
                leadingIcon = Icons.Default.Email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Tanggal Lahir", style = MaterialTheme.typography.labelLarge, modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp))
            TextFieldUI(
                value = birthDate,
                onValueChange = { birthDate = it },
                placeholder = "YYYY-MM-DD",
                leadingIcon = Icons.Default.DateRange,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Kategori Disabilitas", style = MaterialTheme.typography.labelLarge, modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp))
            TextFieldUI(
                value = categoryDisability,
                onValueChange = { categoryDisability = it },
                placeholder = "Masukkan Kategori Disabilitas",
                leadingIcon = Icons.Default.Person,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Nomor Telepon", style = MaterialTheme.typography.labelLarge, modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp))
            TextFieldUI(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                placeholder = "Masukkan Nomor Telepon",
                leadingIcon = Icons.Default.Phone,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Alamat", style = MaterialTheme.typography.labelLarge, modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp))
            TextFieldUI(
                value = address,
                onValueChange = { address = it },
                placeholder = "Masukkan Alamat",
                leadingIcon = Icons.Default.LocationOn,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Password", style = MaterialTheme.typography.labelLarge, modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp))
            TextFieldUI(
                value = password,
                onValueChange = { password = it },
                placeholder = "Masukkan Password",
                leadingIcon = Icons.Default.Lock,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = { // Tambahkan trailingIcon
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Sembunyikan kata sandi" else "Tampilkan kata sandi",
                            tint = colorResource(id = R.color.magenta_80)
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Konfirmasi Password", style = MaterialTheme.typography.labelLarge, modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp))
            TextFieldUI(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = "Masukkan Konfirmasi Password",
                leadingIcon = Icons.Default.Lock,
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = { // Tambahkan trailingIcon
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (confirmPasswordVisible) "Sembunyikan konfirmasi kata sandi" else "Tampilkan konfirmasi kata sandi",
                            tint = colorResource(id = R.color.magenta_80)
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // ... (Sisa kode RegisterScreen tetap sama)
            if (errorMessage != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 8.dp),
                        fontSize = 12.sp,
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = {
                    if (
                        fullName.isNotBlank() && birthDate.isNotBlank() && categoryDisability.isNotBlank() &&
                        phoneNumber.isNotBlank() && address.isNotBlank() &&
                        email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()
                    ) {
                        if (password == confirmPassword) {
                            errorMessage = null
                            coroutineScope.launch {
                                viewModel.signUpAndCreateProfileDirectly(
                                    email = email,
                                    password = password,
                                    fullName = fullName,
                                    birthDate = birthDate,
                                    categoryDisability = categoryDisability,
                                    phoneNumber = phoneNumber,
                                    address = address
                                ).collect { response ->
                                    when (response) {
                                        is AuthResponse.Success -> {
                                            isLoading = false
                                            errorMessage = null
                                            onRegisterSuccess()
                                        }
                                        is AuthResponse.Error -> {
                                            isLoading = false
                                            errorMessage = response.message ?: "Registrasi gagal, silakan coba lagi"
                                        }
                                        is AuthResponse.Loading -> {
                                            isLoading = true
                                        }
                                    }
                                }
                            }
                        } else {
                            errorMessage = "Password tidak cocok"
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
                        modifier = Modifier.size(40.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Daftar",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(vertical = 8.dp),
                        fontSize = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = {
                    errorMessage = null
                    coroutineScope.launch {
                        viewModel.loginWithGoogle().collect { response ->
                            when (response) {
                                is AuthResponse.Success -> {
                                    isLoading = false
                                    viewModel.checkIfUserProfileExists().collect { exists ->
                                        if (exists) {
                                            errorMessage = "Akun Google ini sudah terdaftar. Silakan masuk."
                                        } else {
                                            onNavigateToCompleteProfile()
                                        }
                                    }
                                }
                                is AuthResponse.Error -> {
                                    isLoading = false
                                    errorMessage = response.message ?: "Daftar dengan Google gagal"
                                }
                                is AuthResponse.Loading -> {
                                    isLoading = true
                                }
                            }
                        }
                    }
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                border = ButtonDefaults.outlinedButtonBorder
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Daftar dengan Google",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            TextButton(
                onClick = { onNavigateToLogin() },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Sudah memiliki akun? ")
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.ExtraBold,
                                color = colorResource(id = R.color.magenta_80)
                            )
                        ) {
                            append("Masuk")
                        }
                    },
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}