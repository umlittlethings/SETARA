package com.chrisp.setaraapp.feature.jadwal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.chrisp.setaraapp.feature.auth.AuthViewModel
import com.chrisp.setaraapp.feature.home.HomeViewModel
import com.chrisp.setaraapp.feature.sekerja.SekerjaViewModel
import com.chrisp.setaraapp.feature.sekerja.SekerjaViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseScheduleScreen(
    navController: NavController,
    courseId: String,
    authViewModel: AuthViewModel = viewModel(),
    sekerjaViewModel: SekerjaViewModel = viewModel(
        factory = SekerjaViewModelFactory(authViewModel)
    ),
    homeViewModel: HomeViewModel = viewModel()
) {
    val allSchedules by sekerjaViewModel.schedules.collectAsState()
    val courseSchedules = allSchedules[courseId] ?: emptyList()

    val allCourses = homeViewModel.courses
    val courseTitle = allCourses.find { it.courseId == courseId }?.title ?: "Jadwal Program" //


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(courseTitle, style = MaterialTheme.typography.titleLarge) }, // Apply typography
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp) // Added vertical padding
                .fillMaxSize()
        ) {
            // Use the CourseScheduleView to display the schedules
            CourseScheduleView(schedules = courseSchedules)

            // Add a spacer at the bottom for better visual balance, especially if the list is short
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}