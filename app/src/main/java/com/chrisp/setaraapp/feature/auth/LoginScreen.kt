package com.chrisp.setaraapp.feature.auth // Assuming this is the correct package

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.* // Material (M2) components
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility // Import ikon Visibility
import androidx.compose.material.icons.filled.VisibilityOff // Import ikon VisibilityOff
import androidx.compose.material3.CircularProgressIndicator // Material 3
import androidx.compose.material3.Icon // Import Icon dari M3 untuk trailingIcon
import androidx.compose.material3.IconButton // Import IconButton dari M3
import androidx.compose.material3.MaterialTheme // Material 3
import androidx.compose.material3.OutlinedButton // Material 3
import androidx.compose.material3.Text // Material 3 (prefer this over M2 Text if possible for consistency)
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
import kotlinx.coroutines.launch
import com.chrisp.setaraapp.R
import com.chrisp.setaraapp.component.TextFieldUI

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel = viewModel(),
    onNavigateToRegister: () -> Unit,
    onNavigateToCompleteProfile: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) } // State untuk visibilitas password
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { SnackbarHost(it) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 28.dp),
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
                    text = "Halo, Selamat Datang 👋",
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
                )
                Text(
                    text = "Masuk ke akunmu untuk melanjutkan",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Email",
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
                )
                TextFieldUI(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Masukkan Email",
                    leadingIcon = Icons.Default.Email,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Password",
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
                )
                TextFieldUI(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Masukkan Password",
                    leadingIcon = Icons.Default.Lock,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = { // Tambahkan trailingIcon di sini
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (passwordVisible) "Sembunyikan kata sandi" else "Tampilkan kata sandi",
                                tint = colorResource(id = R.color.magenta_80) // Sesuaikan warna ikon jika perlu
                            )
                        }
                    }
                )

                // ... (Sisa kode LoginScreen tetap sama)
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    TextButton(
                        onClick = { /* TODO: Handle forgot password */ },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = colorResource(id = R.color.magenta_80)
                        ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Lupa Password?",
                            color = colorResource(id = R.color.magenta_80),
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    }
                }

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

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (email.isNotBlank() && password.isNotBlank()) {
                            errorMessage = null
                            coroutineScope.launch {
                                viewModel.signInWithEmail(email, password).collect { response ->
                                    when (response) {
                                        is AuthResponse.Success -> {
                                            isLoading = false
                                            viewModel.checkIfUserProfileExists().collect { exists ->
                                                if (exists) {
                                                    onLoginSuccess()
                                                } else {
                                                    onNavigateToCompleteProfile()
                                                }
                                            }
                                        }
                                        is AuthResponse.Error -> {
                                            isLoading = false
                                            errorMessage = "Login gagal, periksa lagi email dan password anda"
                                        }
                                        is AuthResponse.Loading -> {
                                            isLoading = true
                                        }
                                    }
                                }
                            }
                        } else {
                            errorMessage = "Login gagal, periksa lagi email dan password anda"
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.magenta_80),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(40.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Masuk",
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
                                                onLoginSuccess()
                                            } else {
                                                onNavigateToCompleteProfile()
                                            }
                                        }
                                    }
                                    is AuthResponse.Error -> {
                                        isLoading = false
                                        errorMessage = response.message ?: "Login dengan Google gagal"
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
                    border = BorderStroke(1.dp, colorResource(id = R.color.magenta_80))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Masuk dengan Google",
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                TextButton(
                    onClick = {
                        onNavigateToRegister()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append("Belum memiliki akun? ")
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = colorResource(id = R.color.magenta_80)
                                )
                            ) {
                                append("Daftar")
                            }
                        },
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}