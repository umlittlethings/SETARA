package com.chrisp.setaraapp.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chrisp.setaraapp.Auth.Login.Presentation.LoginScreen
import com.chrisp.setaraapp.Auth.Register.Presentation.SignUpScreen
import com.chrisp.setaraapp.Home.Presentation.HomeScreen

@Composable
fun AuthNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onNavigateToSignUp = { navController.navigate("signup") },
                onLoginSuccess = { navController.navigate("home") }
            )
        }
        composable("signup") {
            SignUpScreen(
                onNavigateToLogin = { navController.navigate("login") },
                onSignUpSuccess = { navController.navigate("home") }
            )
        }
        composable("home") {
            HomeScreen()
        }
    }
}