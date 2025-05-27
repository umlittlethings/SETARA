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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.lifecycle.viewmodel.compose.viewModel // Added for AuthViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.chrisp.setaraapp.component.SearchBarUI
import com.chrisp.setaraapp.navigation.BottomNavigationBar
import com.chrisp.setaraapp.R
import com.chrisp.setaraapp.feature.auth.AuthViewModel // Import AuthViewModel
import com.chrisp.setaraapp.feature.home.HomeViewModel
import com.chrisp.setaraapp.feature.sekerja.model.CourseEnrollment
import com.chrisp.setaraapp.navigation.Screen

val textGreen = Color(0xFF388E3C)
val tagGreenBackground = Color(0xFF4CAF50)
val taskCardBackground = Color(0xFFF3E5F5)
val taskCardIconColor = textGreen
val darkButtonBackground = Color(0xFF2C2C2C)
val lightGrayTextColor = Color(0xFF757575)
val programCardBackgroundColor = Color(0xFFF5F5F5)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SekerjaScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    sekerjaViewModel: SekerjaViewModel = viewModel(
        factory = SekerjaViewModelFactory(authViewModel)
    ),
    homeViewModel: HomeViewModel = viewModel(),
    onDetailTugasClick: () -> Unit
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val enrolledCourses by sekerjaViewModel.enrolledCourses.collectAsState()
    val isLoadingEnrollments by sekerjaViewModel.isLoading
    val enrollmentsError by sekerjaViewModel.errorMessage

    val allCourses = homeViewModel.courses

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        },
        topBar = {
            SekerjaTopAppBar(userName = currentUser?.f_name, onNotificationClick = { navController.navigate(Screen.Notification.route) })
        },
        containerColor = Color.White
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { SearchBarUI() }
            item { HeroTextComponent() }
            item { CategoryButtonsComponent() }
            item {
                SelesaikanTugasmuSection(
                    onDetailTugasClick = onDetailTugasClick
                )
            }
            item { ProgramMuSection(
                enrolledCourses = enrolledCourses,
                allCourses = allCourses, // << TAMBAHKAN allCourses DI SINI
                isLoading = isLoadingEnrollments,
                errorMessage = enrollmentsError,
                onRetry = { sekerjaViewModel.fetchUserEnrollments(currentUser?.id ?: "") }
            ) }
            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}

// Menambahkan ViewModel Factory jika SekerjaViewModel memiliki dependensi di constructornya
// yang tidak bisa di-provide oleh default ViewModelProvider.Factory
// (seperti AuthViewModel)
class SekerjaViewModelFactory(private val authViewModel: AuthViewModel) : Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SekerjaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SekerjaViewModel(authViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SekerjaTopAppBar(userName: String?, onNotificationClick: () -> Unit) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF29B6F6)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userName?.firstOrNull()?.uppercaseChar()?.toString() ?: "U",
                        fontSize = 18.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = userName ?: "Pengguna",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = colorResource(id = R.color.magenta_80)
                )
            }
        },
        actions = {
            IconButton(onClick = onNotificationClick ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
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
        fontSize = 30.sp,
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
            .height(52.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = darkButtonBackground,
            contentColor = Color.White
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
            fontSize = 18.sp,
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
            contentPadding = PaddingValues(end = 4.dp)
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
        modifier = Modifier.width(230.dp),
        shape = RoundedCornerShape(8.dp),
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

// com.chrisp.setaraapp.feature.sekerja.SekerjaScreen.kt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgramMuSection(
    enrolledCourses: List<CourseEnrollment>,
    allCourses: List<com.chrisp.setaraapp.feature.sekerja.model.Course>, // Terima daftar semua course
    isLoading: Boolean,
    errorMessage: String?,
    onRetry: () -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Semua", "Berjalan", "Selesai")

    val displayCourses = remember(selectedTabIndex, enrolledCourses) {
        when (selectedTabIndex) {
            1 -> enrolledCourses.filter { !it.completed } // Anda mungkin ingin filter berdasarkan progress juga
            2 -> enrolledCourses.filter { it.completed }
            else -> enrolledCourses
        }
    }

    Column {
        SectionHeader(title = "Program-mu", titleColor = colorResource(id = R.color.magenta_80), onLihatSemuaClick = { /*TODO*/ })

        PrimaryTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Transparent,
            contentColor = colorResource(id = R.color.magenta_80),
            divider = { }
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
        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = colorResource(id = R.color.magenta_80))
                }
            }
            errorMessage != null -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Text("Gagal memuat: $errorMessage", color = Color.Red)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onRetry) { Text("Coba Lagi") }
                }
            }
            displayCourses.isEmpty() -> {
                Text(
                    if (selectedTabIndex == 0 && enrolledCourses.isEmpty()) "Anda belum mendaftar program apapun."
                    else if (selectedTabIndex == 1) "Tidak ada program yang sedang berjalan."
                    else "Tidak ada program yang sudah selesai.",
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
            }
            else -> {
                displayCourses.forEach { enrollment ->
                    // Cari detail course dari allCourses
                    val courseDetail = allCourses.find { it.courseId == enrollment.courseId }
                    ProgramItemCard(
                        enrollment = enrollment,
                        courseTitle = courseDetail?.title ?: "Judul Tidak Ditemukan", // Tampilkan title
                        courseCompany = courseDetail?.company ?: "Perusahaan Tidak Diketahui" // Tambahkan company jika perlu
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun ProgramItemCard(
    enrollment: CourseEnrollment,
    courseTitle: String, // Parameter baru untuk judul kursus
    courseCompany: String // Parameter baru untuk nama perusahaan
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = programCardBackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Menampilkan nama perusahaan sebagai "jenis" program jika relevan,
                // atau Anda bisa menampilkan tipe program lain jika ada.
                Text(
                    courseCompany, // Gunakan nama perusahaan atau kategori program
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(id = R.color.magenta_80)
                )
                IconButton(onClick = { /*TODO: More options */ }, modifier = Modifier.size(24.dp).offset(y = (-4).dp)) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "More options", tint = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                courseTitle, // Tampilkan judul kursus di sini
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                color = tagGreenBackground,
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    "Status: ${if (enrollment.completed) "Selesai" else "Berjalan"}",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Progress: ${(enrollment.progress * 100).toInt()}%",
                fontSize = 14.sp,
                color = lightGrayTextColor
            )
        }
    }
}


@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun SekerjaScreenPreview() {
    MaterialTheme {
        SekerjaScreen(navController = rememberNavController(), onDetailTugasClick = {})
    }
}