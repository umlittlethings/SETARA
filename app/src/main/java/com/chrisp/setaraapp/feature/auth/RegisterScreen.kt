package com.chrisp.setaraapp.feature.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
     onRegisterSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
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
                .verticalScroll(rememberScrollState()), // Enable scrolling
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Logo
            Image(
                painter = painterResource(id = R.drawable.ic_setara),
                contentDescription = "Setara Logo",
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Header
            Text(
                text = "Buat Akun",
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            Text(
                text = "Buat akun agar kamu bisa mengakses semua fitur di aplikasi ini",
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Full Name Field
            Text(
                text = "Nama Lengkap",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            TextFieldUI(
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = "Masukkan Nama Lengkap",
                leadingIcon = Icons.Default.Person,
                keyboardType = KeyboardType.Text
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email Field
            Text(
                text = "Email",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            TextFieldUI(
                value = email,
                onValueChange = { email = it },
                placeholder = "setara@mail.com",
                leadingIcon = Icons.Default.Email,
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Birth Date Field
            Text(
                text = "Tanggal Lahir",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            TextFieldUI(
                value = birthDate,
                onValueChange = { birthDate = it },
                placeholder = "YYYY-MM-DD",
                leadingIcon = Icons.Default.DateRange,
                keyboardType = KeyboardType.Text
            )

            Spacer(modifier = Modifier.height(16.dp))

            // categoryDisability Field
            Text(
                text = "Kategori Disabilitas",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            TextFieldUI(
                value = categoryDisability,
                onValueChange = { categoryDisability = it },
                placeholder = "Masukkan Kategori Disabilitas",
                leadingIcon = Icons.Default.Person,
                keyboardType = KeyboardType.Text
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Phone Field
            Text(
                text = "Nomor Telepon",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            TextFieldUI(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                placeholder = "Masukkan Nomor Telepon",
                leadingIcon = Icons.Default.Phone,
                keyboardType = KeyboardType.Phone
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Phone Field
            Text(
                text = "Alamat",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            TextFieldUI(
                value = address,
                onValueChange = { address = it },
                placeholder = "Masukkan Alamat",
                leadingIcon = Icons.Default.LocationOn,
                keyboardType = KeyboardType.Text
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            Text(
                text = "Password",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            TextFieldUI(
                value = password,
                onValueChange = { password = it },
                placeholder = "Masukkan Password",
                leadingIcon = Icons.Default.Lock,
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password Field
            Text(
                text = "Konfirmasi Password",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            TextFieldUI(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = "Masukkan Konfirmasi Password",
                leadingIcon = Icons.Default.Lock,
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

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

            // Register Button
            Button(
                onClick = {
                    if (
                        fullName.isNotBlank() && birthDate.isNotBlank() && categoryDisability.isNotBlank() &&
                        phoneNumber.isNotBlank() && address.isNotBlank() &&
                        email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()
                    ) {
                        if (password == confirmPassword) {
                            isLoading = true
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
                                    isLoading = false
                                    when (response) {
                                        is AuthResponse.Success -> {
                                            errorMessage = null
                                            onRegisterSuccess()
                                        }
                                        is AuthResponse.Error -> {
                                            errorMessage = "Registrasi gagal, silakan coba lagi"
                                        }
                                    }
                                }
                            }
                        } else {
                            errorMessage = "Password tidak cocok"
                        }
                    } else {
                        errorMessage = "Isilahkan isi semua kolom"
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
                        text = "Daftar",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(vertical = 8.dp),
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Google Sign-in Button
            OutlinedButton(
                onClick = {
                    isLoading = true
                    coroutineScope.launch {
                        viewModel.loginWithGoogle().collect { response ->
                            isLoading = false
                            when (response) {
                                is AuthResponse.Success -> {
                                    viewModel.checkIfUserProfileExists().collect { exists ->
                                        if (exists) {
                                            onRegisterSuccess()
                                        } else {
                                            onNavigateToLogin()
                                        }
                                    }
                                }
                                is AuthResponse.Error -> {
                                    errorMessage = response.message ?: "Login dengan Google gagal"
                                }
                            }
                        }
                    }
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )

                Text(
                    text = "Daftar dengan Google",
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Already have account
            TextButton(
                onClick = {
                    onNavigateToLogin()
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.Black
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

//@Preview
//@Composable
//private fun RegisterPrev() {
//    RegisterScreen(
//        navController = NavController(context = LocalContext.current),
//        onNavigateToLogin = {},
//        onRegisterSuccess = {}
//    )
//}