package com.chrisp.setaraapp.feature.sekerja.detailTugas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.chrisp.setaraapp.R
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.chrisp.setaraapp.feature.sekerja.detailTugas.model.Assignment

val LightGreenishBackground = Color(0xFFE8F5E9)
val LightGrayUploadArea = Color(0xFFF5F5F5)
val BorderColor = Color(0xFFE0E0E0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTugasScreen(
    navController: NavController,
    courseId: String,
    assignmentId: String,
    viewModel: DetailTugasViewModel = viewModel()
) {
    val assignment by viewModel.assignment
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage
    val isSubmitting by viewModel.isSubmitting
    val submitResult by viewModel.submitResult

    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }

    // File picker launcher
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedFileUri = uri
    }

    LaunchedEffect(Unit) {
        viewModel.fetchAssignmentDetails(courseId, assignmentId)
    }

    Scaffold(
        topBar = {
            DetailTugasTopAppBar(onBackPressed = { navController.popBackStack() })
        },
        bottomBar = {
            assignment?.let {
                DetailTugasBottomBar(
                    onSimpanClick = {
                        selectedFileUri?.let {
                            //TODO
                        }
                    },
                    onBatalClick = { navController.popBackStack() }
                )
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                errorMessage != null -> {
                    Text(
                        text = errorMessage ?: "An unknown error occurred.",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center).padding(16.dp)
                    )
                }
                assignment != null -> {
                    val currentAssignment = assignment!!
                    val createdAtFormatted = try {
                        OffsetDateTime.parse(currentAssignment.createdAt)
                            .format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale("id")))
                    } catch (e: Exception) {
                        currentAssignment.createdAt
                    }
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            HeaderSection(
                                title = currentAssignment.title,
                                courseName = "Semangat!",
                                bannerInfo = "Created: $createdAtFormatted"
                            )
                        }
                        item { Spacer(modifier = Modifier.height(24.dp)) }
                        item {
                            DescriptionSection(
                                description = currentAssignment.description
                            )
                        }
                        item { Spacer(modifier = Modifier.height(24.dp)) }
                        item {
                            InformationSection(
                                title = "Format dan Persyaratan",
                                content = "Detail persyaratan tidak tersedia di model data saat ini."
                            )
                        }
                        item { Spacer(modifier = Modifier.height(24.dp)) }
                        item {
                            AddSubmissionSection(
                                onAddFilesClick = {
                                    filePickerLauncher.launch("*/*") // or "application/pdf" for PDF only
                                }
                            )


                        }
                        item {
                            if (selectedFileUri != null) {
                                Text(
                                    text = "Selected file: $selectedFileUri",
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        item {
                            if (isSubmitting) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                            submitResult?.let {
                                if (it.isSuccess) {
                                    Text(
                                        "Submission successful!",
                                        color = Color(0xFF388E3C),
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                    )
                                } else {
                                    Text(
                                        "Submission failed: ${it.exceptionOrNull()?.message}",
                                        color = Color.Red,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                    )
                                }
                            }
                        }
                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }
                else -> {
                    Text(
                        text = "Assignment data not available.",
                        modifier = Modifier.align(Alignment.Center).padding(16.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTugasTopAppBar(onBackPressed: () -> Unit) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            scrolledContainerColor = Color.White
        )
    )
}

@Composable
fun HeaderSection(title: String, courseName: String, bannerInfo: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = courseName,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = colorResource(id = R.color.magenta_80),
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    colorResource(id = R.color.magenta_80),
                    shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = bannerInfo,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

@Composable
fun DescriptionSection(description: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Deskripsi",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = LightGreenishBackground,
            border = BorderStroke(1.dp, BorderColor.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun InformationSection(title: String, content: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun AddSubmissionSection(onAddFilesClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Add Submission",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = LightGrayUploadArea,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clickable(onClick = onAddFilesClick),
            border = BorderStroke(1.dp, BorderColor.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add files",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Add from files",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun DetailTugasBottomBar(onSimpanClick: () -> Unit, onBatalClick: () -> Unit) {
    Surface(
        shadowElevation = 8.dp,
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onSimpanClick,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.magenta_80)),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text("Simpan", fontWeight = FontWeight.Bold, color = Color.White)
            }
            OutlinedButton(
                onClick = onBatalClick,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.5.dp, colorResource(id = R.color.magenta_80)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = colorResource(id = R.color.magenta_80)),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text("Batal", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailTugasScreenPreview() {
    val previewNavController = rememberNavController()
    DetailTugasScreen(
        navController = previewNavController,
        courseId = "sampleCourse123",
        assignmentId = "sampleAssignment456",
        viewModel = viewModel()
    )
}