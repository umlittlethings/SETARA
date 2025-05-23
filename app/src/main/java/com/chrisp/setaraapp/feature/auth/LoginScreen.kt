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
import androidx.compose.material3.CircularProgressIndicator // Material 3
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
    onLoginSuccess: () -> Unit, // This might need to differentiate between login needing profile completion
    viewModel: AuthViewModel = viewModel(),
    onNavigateToRegister: () -> Unit,
    onNavigateToCompleteProfile: () -> Unit // Added for Google Sign-In scenario
) {
    val scaffoldState = rememberScaffoldState() // M2 ScaffoldState
    val coroutineScope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // M2 Scaffold
    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { SnackbarHost(it) } // M2 SnackbarHost
    ) { paddingValues -> // Renamed to paddingValues to avoid conflict if M3 padding is used
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Use padding from M2 Scaffold
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

                Text( // M3 Text
                    text = "Halo, Selamat Datang 👋",
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
                )
                Text( // M3 Text
                    text = "Masuk ke akunmu untuk melanjutkan",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))

                Text( // M3 Text
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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email) // Corrected usage
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text( // M3 Text
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
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password) // Corrected usage
                )

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    TextButton( // M2 TextButton
                        onClick = { /* TODO: Handle forgot password */ },
                        colors = ButtonDefaults.textButtonColors( // M2 ButtonDefaults
                            contentColor = colorResource(id = R.color.magenta_80)
                        ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text( // M3 Text for consistency, or M2 Text if needed
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
                        Text( // M3 Text
                            text = errorMessage ?: "",
                            color = MaterialTheme.colorScheme.error, // M3 color
                            modifier = Modifier.padding(vertical = 8.dp),
                            fontSize = 12.sp,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Login Button (M2 Button)
                Button(
                    onClick = {
                        if (email.isNotBlank() && password.isNotBlank()) {
                            // isLoading = true // Handled by AuthResponse.Loading
                            errorMessage = null
                            coroutineScope.launch {
                                viewModel.signInWithEmail(email, password).collect { response ->
                                    when (response) {
                                        is AuthResponse.Success -> {
                                            isLoading = false
                                            // Check if profile exists, then decide navigation
                                            viewModel.checkIfUserProfileExists().collect { exists ->
                                                if (exists) {
                                                    onLoginSuccess() // Navigate to Home
                                                } else {
                                                    // This case might be unusual for email/pass login
                                                    // unless profile creation is optional/separate
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
                    colors = ButtonDefaults.buttonColors( // M2 ButtonDefaults
                        backgroundColor = colorResource(id = R.color.magenta_80),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator( // M3 CircularProgressIndicator
                            modifier = Modifier.size(30.dp),
                            color = Color.White, // Explicitly white for on magenta background
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text( // M3 Text for consistency
                            text = "Masuk",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(vertical = 8.dp),
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Google Sign-in Button (M3 OutlinedButton)
                OutlinedButton(
                    onClick = {
                        // isLoading = true // Handled by AuthResponse.Loading
                        errorMessage = null
                        coroutineScope.launch {
                            viewModel.loginWithGoogle().collect { response ->
                                when (response) {
                                    is AuthResponse.Success -> {
                                        isLoading = false
                                        // After Google login, always check if profile needs completion
                                        viewModel.checkIfUserProfileExists().collect { exists ->
                                            if (exists) {
                                                onLoginSuccess() // Navigate to Home
                                            } else {
                                                onNavigateToCompleteProfile() // Navigate to complete profile
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
                    border = BorderStroke(1.dp, colorResource(id = R.color.magenta_80)) // Example border
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp) // Adjusted size
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text
                    Text( // M3 Text
                        text = "Masuk dengan Google", // Changed text slightly for clarity
                        color = Color.Black, // Or MaterialTheme.colorScheme.onSurface
                        fontWeight = FontWeight.Medium,
                        // modifier = Modifier.padding(top = 8.dp, bottom = 8.dp) // Padding handled by button
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Register Prompt (M2 TextButton)
                TextButton(
                    onClick = {
                        onNavigateToRegister()
                    },
                    colors = ButtonDefaults.textButtonColors( // M2 ButtonDefaults
                        contentColor = Color.Black
                    )
                ) {
                    Text( // M3 Text
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