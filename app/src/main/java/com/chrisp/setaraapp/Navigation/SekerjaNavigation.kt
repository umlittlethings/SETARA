package com.chrisp.setaraapp.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chrisp.setaraapp.Sekerja.Presentation.CourseListScreen
import com.chrisp.setaraapp.Sekerja.Presentation.CourseDetailScreen
import com.chrisp.setaraapp.Sekerja.Viewmodel.CourseViewModel

@Composable
fun SekerjaNavigation(viewModel: CourseViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val navController = rememberNavController()
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
                CourseDetailScreen(course = it)
            }
        }
    }
}
