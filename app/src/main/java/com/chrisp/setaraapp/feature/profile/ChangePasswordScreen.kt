package com.chrisp.setaraapp.feature.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.chrisp.setaraapp.R
import com.chrisp.setaraapp.component.TextFieldUI
import com.chrisp.setaraapp.feature.auth.AuthViewModel
import com.chrisp.setaraapp.feature.auth.AuthResponse
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }

    var currentPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmNewPasswordVisible by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Mengamati state dari AuthViewModel untuk update password
    val updatePasswordState by authViewModel.updatePasswordState.collectAsState()

    LaunchedEffect(updatePasswordState) {
        when (val response = updatePasswordState) {
            is AuthResponse.Success -> {
                isLoading = false
                snackbarHostState.showSnackbar(
                    "Kata sandi berhasil diperbarui!",
                    duration = SnackbarDuration.Short
                )
                navController.popBackStack()
                authViewModel.resetUpdatePasswordState() // Reset state setelah berhasil
            }
            is AuthResponse.Error -> {
                isLoading = false
                snackbarHostState.showSnackbar(
                    response.message ?: "Gagal memperbarui kata sandi.",
                    duration = SnackbarDuration.Long
                )
                authViewModel.resetUpdatePasswordState() // Reset state setelah error
            }
            is AuthResponse.Loading -> {
                isLoading = true
            }
            null -> { // Idle state
                isLoading = false
            }
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Ubah Kata Sandi",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Text(
                text = "Kata Sandi",
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Silakan masukkan kata sandi Anda saat ini untuk mengubah kata sandi",
                fontSize = 14.sp,
                color = Color.Gray,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Kata sandi saat ini*",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            TextFieldUI(
                value = currentPassword,
                onValueChange = { currentPassword = it },
                placeholder = "••••••••",
                leadingIcon = Icons.Default.Lock,
                visualTransformation = if (currentPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { currentPasswordVisible = !currentPasswordVisible }) {
                        Icon(
                            imageVector = if (currentPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (currentPasswordVisible) "Sembunyikan kata sandi" else "Tampilkan kata sandi",
                            tint = colorResource(id = R.color.magenta_80)
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Kata sandi baru*",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            TextFieldUI(
                value = newPassword,
                onValueChange = { newPassword = it },
                placeholder = "••••••••",
                leadingIcon = Icons.Default.Lock,
                visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                        Icon(
                            imageVector = if (newPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (newPasswordVisible) "Sembunyikan kata sandi" else "Tampilkan kata sandi",
                            tint = colorResource(id = R.color.magenta_80)
                        )
                    }
                }
            )
            Text(
                "Harus lebih dari 8 karakter.",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Konfirmasi kata sandi baru*",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            TextFieldUI(
                value = confirmNewPassword,
                onValueChange = { confirmNewPassword = it },
                placeholder = "••••••••",
                leadingIcon = Icons.Default.Lock,
                visualTransformation = if (confirmNewPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { confirmNewPasswordVisible = !confirmNewPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmNewPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (confirmNewPasswordVisible) "Sembunyikan kata sandi" else "Tampilkan kata sandi",
                            tint = colorResource(id = R.color.magenta_80)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (currentPassword.isBlank() || newPassword.isBlank() || confirmNewPassword.isBlank()) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Semua kolom kata sandi harus diisi.")
                        }
                        return@Button
                    }
                    if (newPassword.length < 8) { // Supabase default minimal 6, tapi gambar minta 8
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Kata sandi baru minimal 8 karakter.")
                        }
                        return@Button
                    }
                    if (newPassword != confirmNewPassword) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Kata sandi baru dan konfirmasi tidak cocok.")
                        }
                        return@Button
                    }
                    // Panggil fungsi update password dari AuthViewModel
                     authViewModel.updateUserPassword(newPassword = newPassword) // Hapus currentPassword jika tidak diverifikasi di backend
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.magenta_80)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        "Perbarui kata sandi",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = { navController.popBackStack() },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                border = BorderStroke(1.dp, colorResource(id = R.color.magenta_80)),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = colorResource(id = R.color.magenta_80)
                )
            ) {
                Text(
                    "Batal",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun ChangePasswordScreenPreview() {
    MaterialTheme {
        ChangePasswordScreen(navController = rememberNavController())
    }
}