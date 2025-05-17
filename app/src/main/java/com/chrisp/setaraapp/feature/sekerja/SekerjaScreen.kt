package com.chrisp.setaraapp.feature.sekerja

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.chrisp.setaraapp.component.SearchBarUI
import com.chrisp.setaraapp.navigation.BottomNavigationBar
import com.chrisp.setaraapp.R

val textGreen = Color(0xFF388E3C) // Green for "Lihat semua" links and some icons
val tagGreenBackground = Color(0xFF4CAF50) // Green for "Batch 30" tag background
val tagTextWhite = Color.White // Text color for "Batch 30" tag
val taskCardBackground = Color(0xFFF3E5F5) // Light purple-pink for task cards
val taskCardIconColor = textGreen // Icons in task cards are green
val darkButtonBackground = Color(0xFF2C2C2C) // Dark gray for category buttons
val darkButtonText = Color.White // Text color for category buttons
val lightGrayTextColor = Color(0xFF757575) // For less important text like dates
val programCardBackgroundColor = Color(0xFFF5F5F5) // Light gray for program card background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SekerjaScreen(
    navController: NavController,
    onDetailTugasClick: () -> Unit = {},
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        },
        topBar = {
            SekerjaTopAppBar()
        },
        containerColor = Color.White
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp) // Consistent spacing between items
        ) {
            item { SearchBarUI() }
            item { HeroTextComponent() }
            item { CategoryButtonsComponent() }
            item { SelesaikanTugasmuSection(
                onDetailTugasClick = onDetailTugasClick
            ) }
            item { ProgramMuSection() }
            item { Spacer(modifier = Modifier.height(8.dp)) } // Extra space at the end
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SekerjaTopAppBar() {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Placeholder for Avatar Image - In real app, use AsyncImage or painterResource
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF29B6F6)), // Light blue, similar to memoji background
                    contentAlignment = Alignment.Center
                ) {
                    // This is a very rough placeholder for the memoji.
                    // A proper implementation would use an Image composable.
                    Text(
                        "N", // Initial for "Nadia"
                        fontSize = 18.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Nadia",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = colorResource(id = R.color.magenta_80)
                )
            }
        },
        actions = {
            IconButton(onClick = { /* Handle notification click */ }) {
                Icon(
                    imageVector = Icons.Outlined.Notifications, // Or Icons.Default.Notifications
                    contentDescription = "Notifications",
                    tint = colorResource(id = R.color.magenta_80),
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
        )
    )
}

@Composable
fun HeroTextComponent() {
    Text(
        text = buildAnnotatedString {
            append("Jelajahi Kemampuan\nTak Terbatas\ndengan ")
            withStyle(style = SpanStyle(color = colorResource(id = R.color.magenta_80), fontWeight = FontWeight.ExtraBold)) {
                append("SeKerja")
            }
        },
        fontSize = 30.sp, // Adjusted for impact
        fontWeight = FontWeight.Bold,
        lineHeight = 38.sp,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
fun CategoryButtonsComponent() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        CategoryButton("Job Connector Bootcamp")
        CategoryButton("Workshop")
        CategoryButton("Event")
    }
}

@Composable
fun CategoryButton(text: String) {
    Button(
        onClick = { /* TODO: Handle category click */ },
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp), // Standard button height
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = darkButtonBackground,
            contentColor = darkButtonText
        )
    ) {
        Text(text, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun SectionHeader(title: String, titleColor: Color, onLihatSemuaClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 18.sp, // Slightly smaller section title
            fontWeight = FontWeight.Bold,
            color = titleColor
        )
        TextButton(onClick = onLihatSemuaClick) {
            Text("Lihat semua", color = textGreen, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        }
    }
}

@Composable
fun SelesaikanTugasmuSection(
    onDetailTugasClick: () -> Unit = {}
) {
    Column {
        SectionHeader(title = "Selesaikan Tugasmu", titleColor = colorResource(id = R.color.magenta_80), onLihatSemuaClick = { /*TODO*/ })
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(end = 4.dp) // Allow slight peeking
        ) {
            item { TaskCard(
                Icons.Outlined.Schedule,
                "Challenge",
                "Fullstack Web Developer",
                "Selasa, 29 April, 23:59 WIB",
                onClick = {
                    onDetailTugasClick()
                }
                ) }
            item { TaskCard(Icons.Outlined.Assignment, "Assignment", "Fullstack Web Developer", "Selasa, 18 Mei, 23:59 WIB") }
        }
    }
}

@Composable
fun TaskCard(icon: ImageVector, type: String, title: String, deadline: String, onClick: () -> Unit = {}) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(230.dp), // Adjusted width
        shape = RoundedCornerShape(8.dp), // More rounded
        colors = CardDefaults.cardColors(containerColor = taskCardBackground)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = type, tint = taskCardIconColor, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(type, color = taskCardIconColor, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(deadline, fontSize = 12.sp, color = lightGrayTextColor)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgramMuSection() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Semua", "Berjalan", "Selesai")

    Column {
        SectionHeader(title = "Program-mu", titleColor = colorResource(id = R.color.magenta_80), onLihatSemuaClick = { /*TODO*/ })
        // Spacer(modifier = Modifier.height(12.dp)) // Removed spacer, TabRow has its own implicit padding needs

        PrimaryTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Transparent,
            contentColor = colorResource(id = R.color.magenta_80), // For indicator
            divider = { } // No default divider
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            title,
                            color = if (selectedTabIndex == index) colorResource(id = R.color.magenta_80) else Color.Gray,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    selectedContentColor = colorResource(id = R.color.magenta_80),
                    unselectedContentColor = Color.Gray
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp)) // Space between tabs and content

        // Content based on selected tab
        if (selectedTabIndex == 0) { // "Semua" tab
            ProgramItemCard()
        }
        // Add more content for other tabs:
        // if (selectedTabIndex == 1) { /* Berjalan content */ }
        // if (selectedTabIndex == 2) { /* Selesai content */ }
    }
}

@Composable
fun ProgramItemCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = programCardBackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp), // As per image, flat on light gray
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f)) // very subtle border
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top, // Align top for better vertical dot alignment
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Job Connector Bootcamp",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold, // Slightly bolder
                    color = colorResource(id = R.color.magenta_80)
                )
                // IconButton for more options. Ensure it's clickable and has enough touch area.
                IconButton(onClick = { /*TODO: More options */ }, modifier = Modifier.size(24.dp).offset(y = (-4).dp)) { // Offset to align better
                    Icon(Icons.Filled.MoreVert, contentDescription = "More options", tint = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(4.dp)) // Reduced space
            Surface(
                color = tagGreenBackground,
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    "Batch 30",
                    color = tagTextWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                "Fullstack Web Developer",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Periode: 3 Mei - 2 Agt 2025",
                fontSize = 14.sp,
                color = lightGrayTextColor
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun SekerjaScreenPreview() {
    MaterialTheme { // Ensure a MaterialTheme is applied for previews
        SekerjaScreen(navController = rememberNavController())
    }
}