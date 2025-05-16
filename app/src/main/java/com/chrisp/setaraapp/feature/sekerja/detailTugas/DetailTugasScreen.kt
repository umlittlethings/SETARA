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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.chrisp.setaraapp.R

val LightGreenishBackground = Color(0xFFE8F5E9)
val LightGrayUploadArea = Color(0xFFF5F5F5)
val BorderColor = Color(0xFFE0E0E0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTugasScreen(navController: NavController) {
    Scaffold(
        topBar = {
            DetailTugasTopAppBar(onBackPressed = { navController.popBackStack() })
        },
        bottomBar = {
            DetailTugasBottomBar(
                onSimpanClick = { /* TODO: Handle Simpan */ },
                onBatalClick = { navController.popBackStack() }
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            item { HeaderSection() }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item { DescriptionSection() }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item { RequirementsSection() }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item { AddSubmissionSection(onAddFilesClick = { /* TODO: Handle file picker */ }) }
            item { Spacer(modifier = Modifier.height(16.dp)) } // Space before bottom bar
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTugasTopAppBar(onBackPressed: () -> Unit) {
    TopAppBar(
        title = { }, // No title in the app bar itself
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    tint = MaterialTheme.colorScheme.onSurface // Or a specific color
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White, // Match screen background
            scrolledContainerColor = Color.White
        )
    )
}

@Composable
fun HeaderSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp) // Padding for title and subtitle
        ) {
            Text(
                text = "Conditional and loop statements",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp // Slightly larger
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Fullstack Web Developer",
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
                text = "Tenggat waktu: Selasa, 18 Mei, 23.59 WIB",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

@Composable
fun DescriptionSection() {
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
                text = "Sebagai tindak lanjut dari materi yang dipelajari pada sesi \"Conditional and Loop Statements\" dalam Full Stack Developer Bootcamp, peserta diminta untuk mengembangkan sebuah aplikasi sederhana yang memanfaatkan kondisional dan perulangan untuk menyelesaikan masalah praktis. Tujuan dari penugasan ini adalah untuk mengasah keterampilan dalam menerapkan struktur kontrol (seperti if, else, switch, dan loop seperti for, while) dalam membangun aplikasi yang dapat berjalan dengan logika interaktif dan dinamis.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant, // Slightly lighter text
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun RequirementsSection() {
    val requirements = listOf(
        "Tugas ini berupa aplikasi berbasis web.",
        "Gunakan HTML, CSS, dan JavaScript (atau framework/library terkait) dalam pembuatan aplikasi.",
        "File tugas disimpan dalam format ZIP yang berisi seluruh file proyek.",
        "Nama file mengikuti format: Tugas Bootcamp - Full Stack Developer - Conditional Loop - NamaAplikasi.zip",
        "Deadline pengumpulan: 18 Mei 2025", // Assuming the year was missing
        "Dikumpulkan melalui platform yang disediakan oleh penyelenggara bootcamp."
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Format dan Persyaratan",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            requirements.forEachIndexed { index, requirement ->
                Text(
                    text = "${index + 1}. $requirement",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
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

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun DetailTugasScreenPreview() {
    DetailTugasScreen(navController = rememberNavController())
}
