package com.chrisp.setaraapp.Sekerja.Presentation


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.chrisp.setaraapp.Model.DataAuth.Repository.Course
import com.chrisp.setaraapp.Sekerja.Viewmodel.CourseViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CourseListScreen(navController: NavController, viewModel: CourseViewModel = viewModel()) {
    val courses = viewModel.courses
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        courses.forEach { course ->
            CourseItem(course = course) {
                navController.navigate("courseDetail/${course.course_id}")
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun CourseItem(course: Course, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(text = course.title, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = course.detail, style = MaterialTheme.typography.bodySmall)
    }
}