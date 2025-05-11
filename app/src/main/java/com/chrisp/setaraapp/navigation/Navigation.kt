package com.chrisp.setaraapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chrisp.setaraapp.feature.auth.LoginScreen
import com.chrisp.setaraapp.feature.auth.RegisterScreen
import com.chrisp.setaraapp.feature.home.HomeScreen
import com.chrisp.setaraapp.feature.profile.ProfileScreen
import com.chrisp.setaraapp.feature.sekerja.SekerjaScreen
import com.chrisp.setaraapp.feature.sertifikat.SertifikatScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(route = Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {navController.navigate("home")},
                onNavigateToRegister = { navController.navigate("register") },
            )
        }

        composable(route = Screen.Register.route) {
             RegisterScreen(
                 onNavigateToLogin = { navController.navigate("login") },
                 onRegisterSuccess = { navController.navigate("home") },
             )
        }

        composable(route = Screen.Home.route) {
            HomeScreen(
                navController = navController
            )
        }

        composable(route = Screen.Sekerja.route) {
            SekerjaScreen(
                navController = navController
            )
        }

        composable(route = Screen.Sertifikat.route) {
            SertifikatScreen(
                navController = navController
            )
        }

        composable(route = Screen.Profile.route) {
            ProfileScreen(
                navController = navController,
                onLogoutSuccess = {navController.navigate("login")},
            )
        }
    }
}
