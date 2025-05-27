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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.chrisp.setaraapp.R
import com.chrisp.setaraapp.navigation.BottomNavigationBar

// Data class untuk item sertifikat (bisa dipindahkan ke file model)
data class CertificateUIData(
    val id: String,
    val title: String,
    val details: String,
    val iconRes: Int = R.drawable.ic_sertifikat // Default icon dari drawable
)

// Dummy data untuk preview
val dummyCertificates = listOf(
    CertificateUIData(id = "1", title = "Fullstack Web Developer", details = "3.2 Mb, modified Mar 20, 2023"),
    CertificateUIData(id = "2", title = "Fullstack Web Developer", details = "3.2 Mb, modified Mar 20, 2023"),
    CertificateUIData(id = "3", title = "UI/UX Design Fundamental", details = "2.8 Mb, modified Jan 15, 2023"),
    CertificateUIData(id = "4", title = "Data Science with Python", details = "4.1 Mb, modified May 05, 2023")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SertifikatScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        },
        topBar = {
            CenterAlignedTopAppBar( // Menggunakan CenterAlignedTopAppBar agar judul pasti di tengah
                title = {
                    Text(
                        text = "Sertifikat",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface // Atau warna spesifik
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White // Sesuai gambar
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
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                placeholder = { Text("Seminar", color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Search Icon",
                        tint = Color.Gray
                    )
                },
                shape = MaterialTheme.shapes.medium, // Rounded corners
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(id = R.color.magenta_80),
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = colorResource(id = R.color.magenta_80),
                ),
                singleLine = true
            )

            // Daftar Sertifikat
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(dummyCertificates.filter { it.title.contains(searchQuery, ignoreCase = true) || it.details.contains(searchQuery, ignoreCase = true) }) { certificate ->
                    CertificateListItem(certificate = certificate, onDownloadClick = {
                        // TODO: Implement download logic
                    })
                    Divider(color = Color.LightGray.copy(alpha = 0.5f))
                }
            }
        }
    }
}

@Composable
fun CertificateListItem(certificate: CertificateUIData, onDownloadClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = certificate.iconRes),
            contentDescription = certificate.title,
            tint = colorResource(id = R.color.magenta_80), // Warna ikon dari gambar
            modifier = Modifier.size(28.dp) // Ukuran ikon sesuai gambar
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = certificate.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = certificate.details,
                fontSize = 13.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        IconButton(onClick = onDownloadClick) {
            Icon(
                imageVector = Icons.Outlined.CloudDownload, // Ikon unduh
                contentDescription = "Download Sertifikat",
                tint = Color.Gray, // Warna ikon unduh dari gambar
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

