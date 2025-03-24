package com.chrisp.setaraapp.auth.register.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chrisp.setaraapp.R
import com.chrisp.setaraapp.component.ButtonUI
import com.chrisp.setaraapp.component.TextFieldUI

@Composable
fun RegisterScreen() {

    val (email, setEmail) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }
    val (confirmPassword, setConfirmPassword) = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
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
                onValueChange = setEmail,
                placeholder = "Masukkan Email",
                leadingIcon = Icons.Default.Email,
                keyboardType = KeyboardType.Email
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
                onValueChange = setPassword,
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
                onValueChange = setConfirmPassword,
                placeholder = "Masukkan Konfirmasi Password",
                leadingIcon = Icons.Default.Lock,
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Register Button
            ButtonUI(
                text = "Daftar",
                backgroundColor = colorResource(id = R.color.magenta_80),
                textColor = Color.White,
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    // Handle Register button click
                },
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Google Sign-in Button
            OutlinedButton(
                onClick = {
                    // Handle Google sign-in button click
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
                    // Handle "Already have account" click
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

@Preview
@Composable
private fun RegisterPrev() {
    RegisterScreen()
}