package com.chrisp.setaraapp.feature.sertifikat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.chrisp.setaraapp.R
import com.chrisp.setaraapp.feature.auth.AuthViewModel
import com.chrisp.setaraapp.navigation.BottomNavigationBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SertifikatScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    sertifikatViewModel: SertifikatViewModel = viewModel(
        factory = SertifikatViewModelFactory(authViewModel)
    )
) {
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current

    val currentUser by authViewModel.currentUser.collectAsState()
    val certificates by sertifikatViewModel.certificates.collectAsState()
    val isLoading by sertifikatViewModel.isLoading.collectAsState()
    val errorMessage by sertifikatViewModel.errorMessage.collectAsState()

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            sertifikatViewModel.fetchCertificates()
        }
    }

    val filteredCertificates = remember(searchQuery, certificates) {
        if (searchQuery.isBlank()) {
            certificates
        } else {
            certificates.filter {
                it.courseTitle.contains(searchQuery, ignoreCase = true) ||
                it.courseCompany.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Sertifikat",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                placeholder = { Text("Cari sertifikat...", color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Search Icon",
                        tint = Color.Gray
                    )
                },
                shape = MaterialTheme.shapes.medium,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(id = R.color.magenta_80),
                    unfocusedBorderColor = Color.LightGray
                ),
                singleLine = true
            )

            Box(modifier = Modifier.fillMaxSize()) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "Error: $errorMessage",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .align(Alignment.Center)
                    )
                } else if (filteredCertificates.isEmpty()) {
                     Text(
                        text = if (searchQuery.isNotBlank()) "Sertifikat tidak ditemukan" else "Anda belum memiliki sertifikat",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .align(Alignment.Center),
                        color = Color.Gray
                    )
                }
                else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredCertificates, key = { it.courseId }) { certificate ->
                            CertificateListItem(
                                certificate = certificate,
                                onDownloadClick = {
                                    sertifikatViewModel.downloadCertificate(context, certificate)
                                }
                            )
                            Divider(color = Color.LightGray.copy(alpha = 0.5f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CertificateListItem(certificate: Sertifikat, onDownloadClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_sertifikat),
            contentDescription = certificate.courseTitle,
            tint = colorResource(id = R.color.magenta_80),
            modifier = Modifier.size(28.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = certificate.courseTitle,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Selesai pada: ${(certificate.enrollmentDate.take(10))}",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        IconButton(onClick = onDownloadClick) {
            Icon(
                imageVector = Icons.Outlined.CloudDownload,
                contentDescription = "Download Sertifikat",
                tint = Color.Gray,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun SertifikatScreenPreview() {
    MaterialTheme {
        SertifikatScreen(navController = rememberNavController())
    }
}