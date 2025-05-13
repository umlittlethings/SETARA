package com.chrisp.setaraapp.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.chrisp.setaraapp.R
import com.chrisp.setaraapp.feature.auth.AuthViewModel

@Composable
fun SplashScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val isLoggedIn = remember { mutableStateOf<Boolean?>(null) }

    // White background with centered logo
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground), // Replace with your actual logo resource
                contentDescription = "App Logo",
                modifier = Modifier.size(300.dp) // Adjust size as needed
            )
        }
    }

    // Navigation logic
    LaunchedEffect(Unit) {
        authViewModel.isUserLoggedIn().collect { loggedIn ->
            isLoggedIn.value = loggedIn
        }
    }

    isLoggedIn.value?.let { loggedIn ->
        LaunchedEffect(loggedIn) {
            if (loggedIn) {
                navController.navigate("home") {
                    popUpTo("splash") { inclusive = true }
                }
            } else {
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }
    }
}