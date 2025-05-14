package com.chrisp.setaraapp.feature.cvGenerate.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.chrisp.setaraapp.R
import com.chrisp.setaraapp.feature.cvGenerate.presentation.component.EducationDataSection
import com.chrisp.setaraapp.feature.cvGenerate.presentation.component.PersonalDataSection
import com.chrisp.setaraapp.feature.cvGenerate.presentation.component.WorkExperienceDataSection

@Composable
fun FormScreen(
    viewModel: FormViewModel = viewModel(),
    navController: NavController,
    onNavigateToPreview: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            FormTopBar(
                currentPage = uiState.currentPage,
                viewModel = viewModel,
                navController = navController
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Progress Indicator
            LinearProgressIndicator(
                progress = { (uiState.currentPage + 1) / 3f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = Color(0xFF62d083),
                trackColor = Color.LightGray
            )

            // Current Form Page
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (uiState.currentPage) {
                    0 -> PersonalDataSection(viewModel)
                    1 -> EducationDataSection(viewModel)
                    2 -> WorkExperienceDataSection(viewModel)
                }
            }

            // Next Button
            Button(
                onClick = {
                    val isValid = when (uiState.currentPage) {
                        0 -> viewModel.validatePersonalData()
                        1 -> viewModel.validateEducation()
                        2 -> viewModel.validateWorkExperience()
                        else -> true
                    }
                    if (isValid) {
                        if (uiState.currentPage == 2) {
                            onNavigateToPreview()
                        } else {
                            viewModel.navigateToNextPage()
                        }
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Harap isi semua kolom yang diperlukan!")
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.magenta_80)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(16.dp, 0.dp, 16.dp, 8.dp)
            ) {
                Text(
                    text = "Berikutnya",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(vertical = 8.dp),
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormTopBar(currentPage: Int, viewModel: FormViewModel, navController: NavController) {
    TopAppBar(
        title = {
            Text(
                text = when (currentPage) {
                    0 -> "Data diri"
                    1 -> "Pendidikan"
                    else -> "Pengalaman Kerja"
                },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                if (currentPage > 0) {
                    viewModel.navigateToPreviousPage()
                } else {
                    navController.popBackStack()
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back"
                )
            }
        }
    )
}