package com.chrisp.setaraapp.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.chrisp.setaraapp.navigation.BottomNavigationBar
import com.chrisp.setaraapp.R
import com.chrisp.setaraapp.feature.auth.AuthViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel(),
    onLogoutSuccess: () -> Unit = {},
    onCvGeneration: () -> Unit = {}
    ) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        },
        containerColor = Color.White
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(bottom = innerPadding.calculateBottomPadding())
                .fillMaxSize()
        ) {
            item {
                ProfileHeader()
            }
            item {
                ProfileSection(
                    title = "Akun",
                    items = listOf(
                        ProfileItemData("Curriculum Vitae Generate", Icons.Outlined.Article) {
                            onCvGeneration()
                        },
                        ProfileItemData("Reset Password", Icons.Outlined.Shield) {},
                        ProfileItemData("Hapus Akun", Icons.Outlined.Delete) {},
                        ProfileItemData("Keluar", Icons.Outlined.Logout) {
                            viewModel.logout()
                            onLogoutSuccess()
                        }
                    )
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                ProfileSection(
                    title = "Info lainnya",
                    items = listOf(
                        ProfileItemData("Tentang Kami", Icons.Outlined.Business) {},
                        ProfileItemData("Pusat Bantuan", Icons.Outlined.Help) {}
                    )
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ProfileHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.magenta_80))
            .padding(top = 32.dp, bottom = 24.dp, start = 16.dp, end = 16.dp) // Adjust top padding for status bar
    ) {
        // Settings Icon
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.borderPink)),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(48.dp),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            IconButton(
                onClick = { /* TODO: Handle Settings Click */ },
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings",
                    tint = colorResource(id = R.color.magenta_80),
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Placeholder for Avatar Image - In real app, use AsyncImage or painterResource
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF29B6F6)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "N", // Initial for "Nadia"
                    fontSize = 30.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
//            Image(
//                painter = painterResource(id = R.drawable.ic_profile), // Replace with actual image
//                contentDescription = "User Avatar",
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .size(100.dp)
//                    .clip(CircleShape)
//                    .background(Color.LightGray) // Placeholder background
//            )
            Text(
                text = "Nadia",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "nadia@mail.com",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

data class ProfileItemData(
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@Composable
fun ProfileSection(title: String, items: List<ProfileItemData>) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        items.forEach { item ->
            ProfileMenuItem(label = item.label, icon = item.icon, onClick = item.onClick)
            Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 0.5.dp)
        }
    }
}

@Composable
fun ProfileMenuItem(label: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = colorResource(id = R.color.black),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            color = colorResource(id = R.color.black),
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = "Navigate",
            tint = colorResource(id = R.color.black).copy(alpha = 0.7f)
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreen(navController = rememberNavController())
    }
}