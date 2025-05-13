package com.chrisp.setaraapp.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chrisp.setaraapp.Sekerja.Presentation.CourseListScreen
import com.chrisp.setaraapp.Sekerja.Presentation.CourseDetailScreen
import com.chrisp.setaraapp.Sekerja.Presentation.EnrollmentConfirmationScreen
import com.chrisp.setaraapp.Sekerja.Viewmodel.CourseViewModel

@Composable
fun SekerjaNavigation(viewModel: CourseViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val navController = rememberNavController()

    // Load user enrollment data when navigation is created
    LaunchedEffect(Unit) {
        viewModel.checkUserEnrollment()
    }

    NavHost(navController = navController, startDestination = "courseList") {
        composable("courseList") {
            CourseListScreen(navController, viewModel)
        }

        composable(
            route = "courseDetail/{courseId}",
            arguments = listOf(navArgument("courseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId")
            val course = viewModel.courses.find { it.course_id == courseId }
            course?.let {
                CourseDetailScreen(
                    course = it,
                    viewModel = viewModel,
                    onEnrollmentSuccess = {
                        navController.navigate("enrollmentConfirmation")
                    }
                )
            }
        }

        composable("enrollmentConfirmation") {
            EnrollmentConfirmationScreen(
                onNavigateBack = {
                    navController.popBackStack(route = "courseList", inclusive = false)
                }
            )
        }
    }
}