package com.chrisp.setaraapp.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chrisp.setaraapp.feature.auth.AuthViewModel
import com.chrisp.setaraapp.feature.auth.CompleteProfileScreen
import com.chrisp.setaraapp.feature.auth.LoginScreen
import com.chrisp.setaraapp.feature.auth.RegisterScreen
import com.chrisp.setaraapp.feature.cvGenerate.presentation.CvFeature
import com.chrisp.setaraapp.feature.sekerja.detailProgram.DetailProgramScreen
import com.chrisp.setaraapp.feature.home.HomeScreen
import com.chrisp.setaraapp.feature.home.HomeViewModel
import com.chrisp.setaraapp.feature.jadwal.CourseScheduleScreen
import com.chrisp.setaraapp.feature.notification.NotificationScreen
import com.chrisp.setaraapp.feature.onboarding.OnboardingPreferences
import com.chrisp.setaraapp.feature.onboarding.OnboardingScreen
import com.chrisp.setaraapp.feature.profile.ChangePasswordScreen
import com.chrisp.setaraapp.feature.profile.ProfileScreen
import com.chrisp.setaraapp.feature.sekerja.SekerjaScreen
import com.chrisp.setaraapp.feature.sekerja.detailProgram.EnrollmentSuccessScreen
import com.chrisp.setaraapp.feature.sekerja.detailTugas.DetailTugasScreen
import com.chrisp.setaraapp.feature.sertifikat.SertifikatScreen
import com.chrisp.setaraapp.feature.splash.SplashScreen
import com.chrisp.setaraapp.feature.sekerja.detailTugas.DetailTugasViewModel
import com.chrisp.setaraapp.feature.sertifikat.SertifikatViewModelFactory

@Composable
fun Navigation() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val homeViewModel: HomeViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
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

        composable(
            route = "${Screen.CourseSchedule.route}/{courseId}",
            arguments = listOf(navArgument("courseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId")
            if (courseId != null) {
                CourseScheduleScreen(
                    navController = navController,
                    courseId = courseId
                )
            } else {
                // Handle kasus courseId null jika diperlukan, misal kembali ke halaman sebelumnya
                navController.popBackStack()
            }
        }

        composable(route = Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onNavigateToCompleteProfile = {
                    navController.navigate(Screen.CompleteProfile.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToCompleteProfile = {
                    navController.navigate(Screen.CompleteProfile.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.CompleteProfile.route) {
            CompleteProfileScreen(
                onComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.CompleteProfile.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.Home.route) {
            HomeScreen(
                navController = navController,
                onNavigateToDetail = { courseId ->
                    navController.navigate(Screen.DetailProgram.createRoute(courseId))
                }
            )
        }

        composable(route = Screen.Sekerja.route) {
            SekerjaScreen(
                navController = navController
            )
        }

        composable(route = Screen.Sertifikat.route) {
            SertifikatScreen(
                navController = navController,
                authViewModel = authViewModel,
                sertifikatViewModel = viewModel(
                    factory = SertifikatViewModelFactory(authViewModel)
                )
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

        composable(
            route = Screen.DetailProgram.route,
            arguments = listOf(navArgument("courseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId")
            val course = homeViewModel.courses.find { it.courseId == courseId }
            course?.let {
                DetailProgramScreen(
                    courseId = it,
                    onEnrollmentSuccess = {
                        navController.navigate(Screen.EnrollmentSuccess.route) {
                            popUpTo(Screen.DetailProgram.route) { inclusive = true }
                        }
                    },
                )
            }
        }

        composable(
            route = "${Screen.DetailTugas.route}/{courseId}/{assignmentId}",
            arguments = listOf(
                navArgument("courseId") { type = NavType.StringType },
                navArgument("assignmentId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId")
            val assignmentId = backStackEntry.arguments?.getString("assignmentId")

            if (courseId != null && assignmentId != null) {
                // You'd typically inject the ViewModel using Hilt or pass factory
                val detailTugasViewModel: DetailTugasViewModel = viewModel() // Or however you instantiate
                DetailTugasScreen(
                    navController = navController,
                    courseId = courseId,
                    assignmentId = assignmentId,
                    viewModel = detailTugasViewModel
                )
            } else {
                // Handle error or navigate back if IDs are missing
                Text("Error: Missing courseId or assignmentId")
            }
        }

        composable(route = Screen.EnrollmentSuccess.route) {
            EnrollmentSuccessScreen(
                onSelesaiClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.EnrollmentSuccess.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.Notification.route) {
            NotificationScreen(navController = navController)
        }

        composable(route = Screen.ChangePassword.route) {
            ChangePasswordScreen(navController = navController)
        }
    }
}
