package com.chrisp.setaraapp.feature.jadwal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.chrisp.setaraapp.feature.home.HomeViewModel // <-- 1. TAMBAHKAN IMPORT INI
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
    homeViewModel: HomeViewModel = viewModel() // <-- 2. TAMBAHKAN HomeViewModel
) {
    // Ambil data jadwal dari ViewModel
    val allSchedules by sekerjaViewModel.schedules.collectAsState()
    val courseSchedules = allSchedules[courseId] ?: emptyList()

    // 3. PERBAIKI LOGIKA PENGAMBILAN JUDUL
    // Ambil daftar semua kursus dari HomeViewModel
    val allCourses = homeViewModel.courses
    // Cari judul kursus berdasarkan courseId
    val courseTitle = allCourses.find { it.courseId == courseId }?.title ?: "Jadwal Program"


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(courseTitle) },
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
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize()
        ) {
            // Gunakan Composable view yang sudah dibuat di langkah sebelumnya
            CourseScheduleView(schedules = courseSchedules)
        }
    }
}