package com.chrisp.setaraapp.feature.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import kotlinx.coroutines.launch
import com.chrisp.setaraapp.R
import com.chrisp.setaraapp.component.TextFieldUI

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel = viewModel(),
    onNavigateToRegister: () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { SnackbarHost(it) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
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

                // Logo
                Image(
                    painter = painterResource(id = R.drawable.ic_setara),
                    contentDescription = "Setara Logo",
                    modifier = Modifier.size(100.dp)
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Header
                Text(
                    text = "Halo, Selamat Datang👋",
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                )

                Text(
                    text = "Masuk ke akunmu untuk melanjutkan",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

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
                    placeholder = "Masukkan Email",
                    leadingIcon = Icons.Default.Email,
                    keyboardType = KeyboardType.Email,
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
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                )

                // Forgot Password
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

                // Login Button
                Button(
                    onClick = {
                        if (email.isNotBlank() && password.isNotBlank()) {
                            isLoading = true
                            coroutineScope.launch {
                                viewModel.signInWithEmail(email, password).collect { response ->
                                    isLoading = false
                                    when (response) {
                                        is AuthResponse.Success -> {
                                            errorMessage = null
                                            onLoginSuccess()
                                        }
                                        is AuthResponse.Error -> {
                                            errorMessage = "Login gagal, periksa lagi email dan password anda"
                                        }
                                    }
                                }
                            }
                        } else {
                            errorMessage = "Silakan isi email dan password"
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
                            color = MaterialTheme.colorScheme.onPrimary,
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
                                                onLoginSuccess()
                                            } else {
                                                onLoginSuccess()
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

                // Register Prompt
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