package com.chrisp.setaraapp.feature.notification

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack // Import ikon kembali yang benar
import androidx.compose.material.icons.filled.Info // Anda mungkin perlu ikon info yang spesifik
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.chrisp.setaraapp.R

// Data class untuk item notifikasi
data class NotificationItemData(
    val id: String,
    val type: String, // "Info", "Peringatan", dll.
    val title: String,
    val message: String,
    val timestamp: String,
    val icon: ImageVector = Icons.Filled.Info, // Default icon
    val backgroundColor: Color // Warna latar belakang kartu notifikasi
)

// Dummy data untuk notifikasi
val dummyNotifications = listOf(
    NotificationItemData(
        id = "1",
        type = "Info",
        title = "Security Alert",
        message = "Kami menemukan info masuk baru ke akun Anda di perangkat lain. Jika ini Anda, Anda tidak perlu melakukan apa pun. Jika tidak, kami akan membantu mengamankan akun Anda.",
        timestamp = "31-11-2022 09:41",
        backgroundColor = Color.LightGray.copy(alpha = 0.2f)
    ),
    NotificationItemData(
        id = "2",
        type = "Info",
        title = "Security Alert",
        message = "Kami menemukan info masuk baru ke akun Anda di perangkat lain. Jika ini Anda, Anda tidak perlu melakukan apa pun. Jika tidak, kami akan membantu mengamankan akun Anda.",
        timestamp = "31-11-2022 09:41",
        backgroundColor = Color.LightGray.copy(alpha = 0.2f)
    )
    // Tambahkan notifikasi lain jika perlu
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Notifikasi",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface // Warna teks default
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Menggunakan ikon kembali yang benar
                            contentDescription = "Kembali",
                            tint = MaterialTheme.colorScheme.onSurface // Warna ikon default
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White // Latar belakang TopAppBar putih
                )
            )
        },
        containerColor = Color.White // Latar belakang Scaffold putih
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp) // Padding vertikal untuk daftar
        ) {
            items(dummyNotifications, key = { it.id }) { notificationItem ->
                NotificationListItem(item = notificationItem)
                Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 0.5.dp)
            }
        }
    }
}

@Composable
fun NotificationListItem(item: NotificationItemData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp), // Padding untuk setiap kartu
        shape = MaterialTheme.shapes.medium, // Sedikit lengkungan pada kartu
        colors = CardDefaults.cardColors(containerColor = item.backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // Tidak ada shadow sesuai gambar
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top // Ikon sejajar dengan teks atas
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.type,
                tint = colorResource(id = R.color.magenta_80), // Warna ikon info dari gambar
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape) // Ikon dalam lingkaran jika diinginkan (atau hilangkan jika tidak)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = item.type,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.magenta_80) // Warna teks "Info"
                )
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface // Warna teks judul
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.message,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant, // Warna teks pesan yang sedikit lebih terang
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item.timestamp,
                    fontSize = 12.sp,
                    color = Color.Gray // Warna teks timestamp
                )
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun NotificationScreenPreview() {
    MaterialTheme {
        NotificationScreen(navController = rememberNavController())
    }
}