package com.chrisp.setaraapp.Navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chrisp.setaraapp.Auth.Login.Presentation.CompleteProfileScreen
import com.chrisp.setaraapp.Auth.Login.Presentation.LoginScreen
import com.chrisp.setaraapp.Auth.Register.Presentation.SignUpScreen
import com.chrisp.setaraapp.Auth.ViewModel.AuthViewModel
import com.chrisp.setaraapp.Home.Presentation.HomeScreen

@Composable
fun AuthNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onNavigateToSignUp = { navController.navigate("signup") },
                onLoginSuccess = { navController.navigate("home") },
                onNavigateToCompleteProfile = { navController.navigate("completeProfile") }
            )
        }
        composable("signup") {
            SignUpScreen(
                onNavigateToLogin = { navController.navigate("login") },
                onSignUpSuccess = { navController.navigate("home") }
            )
        }
        composable("completeProfile") {
            CompleteProfileScreen(
                onComplete = { navController.navigate("home") }
            )
        }

        composable("courseList") {
            SekerjaNavigation() // Atau langsung panggil CourseListScreen jika ingin tanpa nav di dalam nav
        }
        composable("home") {
            val context = LocalContext.current
            val authViewModel = viewModel<AuthViewModel>(
                factory = ViewModelProvider.AndroidViewModelFactory(context.applicationContext as Application)
            )
            HomeScreen(
                authViewModel = authViewModel,
                onLogoutSuccess = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                navToCourseList = {
                    navController.navigate("courseList")
                }
            )
        }



    }
}
