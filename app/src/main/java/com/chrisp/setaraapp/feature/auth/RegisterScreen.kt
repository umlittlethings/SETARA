package com.chrisp.setaraapp.feature.auth // Assuming this is the correct package

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions // Import KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.* // Keep specific imports or use *
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.input.KeyboardType // Import KeyboardType
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
    onRegisterSuccess: () -> Unit, // This might navigate to Home or CompleteProfile
    onNavigateToCompleteProfile: () -> Unit // Added for Google Sign-Up if profile is needed
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
            .background(Color.White), // Or MaterialTheme.colorScheme.background
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
                color = Color.Gray, // Or MaterialTheme.colorScheme.onSurfaceVariant
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text) // Or Number for parsing
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Kategori Disabilitas", style = MaterialTheme.typography.labelLarge, modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp))
            TextFieldUI(
                value = categoryDisability,
                onValueChange = { categoryDisability = it },
                placeholder = "Masukkan Kategori Disabilitas",
                leadingIcon = Icons.Default.Person, // Consider specific icon
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
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Konfirmasi Password", style = MaterialTheme.typography.labelLarge, modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp))
            TextFieldUI(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = "Masukkan Konfirmasi Password",
                leadingIcon = Icons.Default.Lock,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
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
                            // isLoading = true // Handled by AuthResponse.Loading
                            errorMessage = null
                            coroutineScope.launch {
                                // Ensure this ViewModel function exists and emits AuthResponse
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
                                            onRegisterSuccess() // Navigate to Home or success screen
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
                    containerColor = colorResource(id = R.color.magenta_80) // Ensure color name is correct
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(40.dp),
                        color = MaterialTheme.colorScheme.onPrimary, // Or Color.White
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Daftar",
                        color = Color.White, // Or MaterialTheme.colorScheme.onPrimary
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
                    // isLoading = true // Handled by AuthResponse.Loading
                    errorMessage = null
                    coroutineScope.launch {
                        viewModel.loginWithGoogle().collect { response ->
                            when (response) {
                                is AuthResponse.Success -> {
                                    isLoading = false
                                    // After Google login, check if profile needs completion for registration flow
                                    viewModel.checkIfUserProfileExists().collect { exists ->
                                        if (exists) {
                                            // User already exists, perhaps navigate to login or show message
                                            errorMessage = "Akun Google ini sudah terdaftar. Silakan masuk."
                                            // onNavigateToLogin() // Option: navigate to login
                                        } else {
                                            // New Google user, proceed to complete profile
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
                border = ButtonDefaults.outlinedButtonBorder // Use M3 default or customize
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp) // Adjusted size
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Daftar dengan Google",
                    color = MaterialTheme.colorScheme.onSurface, // Use theme color
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Already have account
            TextButton(
                onClick = { onNavigateToLogin() },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground // Use theme color
                )
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Sudah memiliki akun? ")
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.ExtraBold,
                                color = colorResource(id = R.color.magenta_80) // Ensure color name
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