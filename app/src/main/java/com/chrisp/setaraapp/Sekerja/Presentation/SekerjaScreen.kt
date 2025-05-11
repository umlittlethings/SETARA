package com.chrisp.setaraapp.Sekerja.Presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chrisp.setaraapp.Model.DataAuth.Repository.Course
import com.chrisp.setaraapp.Model.DataAuth.Repository.Module
import com.chrisp.setaraapp.Sekerja.Viewmodel.CourseViewModel
import java.util.UUID
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun CourseListScreen(viewModel: CourseViewModel = viewModel()) {
    val courses = viewModel.courses
    val expandedCourses = viewModel.expandedCourseIds

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        courses.forEach { course ->
            CourseItem(
                course = course,
                isExpanded = expandedCourses.contains(course.id),
                onToggleExpand = { viewModel.toggleCourseExpansion(course.id) }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}


@Composable
fun CourseItem(
    course: Course,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onToggleExpand() }
            .padding(16.dp)
    ) {
        Text(text = course.title, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = course.description, style = MaterialTheme.typography.bodySmall)

        AnimatedVisibility(visible = isExpanded) {
            Column(modifier = Modifier.padding(top = 12.dp)) {
                course.modules.forEach { module ->
                    ModuleItem(module)
                }
            }
        }
    }
}

@Composable
fun ModuleItem(module: Module) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable { expanded = !expanded }
            .padding(12.dp)
    ) {
        Text(text = "${module.title} (${module.sesi} sesi)", fontWeight = FontWeight.SemiBold)

        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(top = 6.dp)) {
                module.content.forEach {
                    Text(text = "• $it", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
