package com.chrisp.setaraapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chrisp.setaraapp.feature.auth.LoginScreen
import com.chrisp.setaraapp.feature.auth.RegisterScreen
import com.chrisp.setaraapp.feature.cvGenerate.presentation.CvFeature
import com.chrisp.setaraapp.feature.detailProgram.DetailProgramScreen
import com.chrisp.setaraapp.feature.home.HomeScreen
import com.chrisp.setaraapp.feature.onboarding.OnboardingPreferences
import com.chrisp.setaraapp.feature.onboarding.OnboardingScreen
import com.chrisp.setaraapp.feature.profile.ProfileScreen
import com.chrisp.setaraapp.feature.sekerja.SekerjaScreen
import com.chrisp.setaraapp.feature.sertifikat.SertifikatScreen
import com.chrisp.setaraapp.feature.splash.SplashScreen

@Composable
fun Navigation() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val onboardingCompleted = OnboardingPreferences.isOnboardingCompleted(context)

    NavHost(
        navController = navController,
        startDestination = if (onboardingCompleted) {
            Screen.Splash.route
        } else {
            Screen.Onboarding.route
        }
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navController)
        }

        composable(route = Screen.Onboarding.route) {
            OnboardingScreen(
                onFinish = {
                    OnboardingPreferences.setOnboardingCompleted(context, true)
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate( Screen.Home.route ) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
            )
        }

        composable(route = Screen.Register.route) {
             RegisterScreen(
                 onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                 onRegisterSuccess = { navController.navigate(Screen.Home.route){
                        popUpTo(Screen.Register.route) { inclusive = true }
                 } },
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
                onLogoutSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Profile.route) { inclusive = true }
                    }
                },
                onCvGeneration = {
                    navController.navigate(Screen.Cvfeature.route)
                }
            )
        }

        composable(route = Screen.Cvfeature.route) {
            CvFeature(
                onSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Cvfeature.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.DetailProgram.route) {
             DetailProgramScreen(
                 navController = navController
             )
        }
    }
}
