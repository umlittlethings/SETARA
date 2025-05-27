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
import com.chrisp.setaraapp.feature.auth.User // Ensure User model is imported
import com.chrisp.setaraapp.navigation.Screen
import kotlinx.coroutines.flow.firstOrNull


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel(),
    onLogoutSuccess: () -> Unit = {},
    onCvGeneration: () -> Unit = {}
) {
    // Observe user data from AuthViewModel
    val currentUser by viewModel.currentUser.collectAsState()
    // val isUserLoading by viewModel.isUserLoading // If you need to show a loader for profile data
    // val userError by viewModel.userError // If you need to show an error for profile data

    // Optional: If this screen is the first one after app open for a logged-in user,
    // you might need to ensure the profile is fetched.
    // AuthViewModel now attempts to fetch on login/signup and via observeUserLoginStatus.
    LaunchedEffect(key1 = Unit) { // Fetch once when the screen is composed if needed
        if (viewModel.isUserLoggedIn().firstOrNull() == true && currentUser == null) {
            viewModel.fetchCurrentUserProfile()
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        },
        containerColor = Color.White
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                // Apply only bottom padding from Scaffold, as TopAppBar has its own system
                .padding(bottom = innerPadding.calculateBottomPadding())
                .fillMaxSize()
        ) {
            item {
                // Pass dynamic user data to ProfileHeader
                ProfileHeader(
                    userName = currentUser?.f_name,
                    userEmail = currentUser?.email
                )
            }
            item {
                ProfileSection(
                    title = "Akun",
                    items = listOf(
                        ProfileItemData("Curriculum Vitae Generate", Icons.Outlined.Article) {
                            onCvGeneration()
                        },
                        ProfileItemData("Reset Password", Icons.Outlined.Shield) { navController.navigate(Screen.ChangePassword.route) },
                        ProfileItemData("Hapus Akun", Icons.Outlined.Delete) { /* TODO */ },
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
                        ProfileItemData("Tentang Kami", Icons.Outlined.Business) { /* TODO */ },
                        ProfileItemData("Pusat Bantuan", Icons.Outlined.Help) { /* TODO */ }
                    )
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp)) // For scroll padding at the bottom
            }
        }
    }
}

@Composable
fun ProfileHeader(userName: String?, userEmail: String?) { // Accept userName and userEmail
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.magenta_80))
            // paddingTop should account for status bar if your app is edge-to-edge
            .padding(top = 32.dp, bottom = 24.dp, start = 16.dp, end = 16.dp)
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
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF29B6F6)), // Placeholder avatar background
                contentAlignment = Alignment.Center
            ) {
                Text(
                    // Display first letter of userName if available, else a default
                    text = userName?.firstOrNull()?.uppercaseChar()?.toString() ?: "U",
                    fontSize = 30.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                // Display userName if available, else "Pengguna" or "Loading..."
                text = userName ?: "Pengguna",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                // Display userEmail if available, else a placeholder
                text = userEmail ?: "email@example.com",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

// --- ProfileItemData, ProfileSection, ProfileMenuItem remain the same ---
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
            color = Color.Black, // Consider MaterialTheme.colorScheme.onSurface
            modifier = Modifier.padding(bottom = 12.dp)
        )
        items.forEachIndexed { index, item -> // Use forEachIndexed for key if needed
            ProfileMenuItem(label = item.label, icon = item.icon, onClick = item.onClick)
            if (index < items.lastIndex) { // Add divider except for the last item
                Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 0.5.dp)
            }
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
            tint = colorResource(id = R.color.black), // Consider MaterialTheme.colorScheme.onSurfaceVariant
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            color = colorResource(id = R.color.black), // Consider MaterialTheme.colorScheme.onSurface
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = "Navigate",
            tint = colorResource(id = R.color.black).copy(alpha = 0.7f) // Consider MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme { // Wrap preview in MaterialTheme
        ProfileScreen(navController = rememberNavController())
    }
}