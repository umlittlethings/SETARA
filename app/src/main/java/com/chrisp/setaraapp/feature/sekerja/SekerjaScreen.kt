package com.chrisp.setaraapp.feature.sekerja

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.chrisp.setaraapp.R
import com.chrisp.setaraapp.component.SearchBarUI
import com.chrisp.setaraapp.feature.auth.AuthViewModel
import com.chrisp.setaraapp.feature.home.HomeViewModel
import com.chrisp.setaraapp.feature.sekerja.detailTugas.model.Assignment
import com.chrisp.setaraapp.feature.sekerja.model.Course
import com.chrisp.setaraapp.feature.sekerja.model.CourseEnrollment
import com.chrisp.setaraapp.navigation.BottomNavigationBar
import com.chrisp.setaraapp.navigation.Screen
import kotlinx.coroutines.launch

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
    homeViewModel: HomeViewModel = viewModel()
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val enrolledCourses by sekerjaViewModel.enrolledCourses.collectAsState()
    val isLoadingEnrollments by sekerjaViewModel.isLoading
    val enrollmentsError by sekerjaViewModel.errorMessage
    val assignments by sekerjaViewModel.assignments.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val allCourses = homeViewModel.courses
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(currentUser?.id) {
        currentUser?.id?.let { userId ->
            sekerjaViewModel.generateMissingSubmissions(userId)
            if (enrolledCourses.isEmpty() && !isLoadingEnrollments) {
                sekerjaViewModel.fetchUserEnrollments(userId)
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        },
        topBar = {
            SekerjaTopAppBar(
                userName = currentUser?.f_name,
                onNotificationClick = { navController.navigate(Screen.Notification.route) },
                searchQuery = searchQuery,
                onSearchQueryChanged = { newQuery ->
                    val oldQueryIsEmpty = searchQuery.isBlank()
                    searchQuery = newQuery
                    val newQueryIsEmpty = newQuery.isBlank()

                    if (!newQueryIsEmpty) {
                        coroutineScope.launch {
                            lazyListState.animateScrollToItem(index = 0)
                        }
                    } else if (newQueryIsEmpty && !oldQueryIsEmpty) {
                        coroutineScope.launch {
                            lazyListState.animateScrollToItem(index = 0)
                        }
                    }
                }
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (searchQuery.isBlank()){
                item { HeroTextComponent() }
                item { CategoryButtonsComponent() }
                item {
                    SelesaikanTugasmuSection(
                        assignments = assignments,
                        onDetailTugasClick = { courseId, assignmentId ->
                            navController.navigate("${Screen.DetailTugas.route}/$courseId/$assignmentId")
                        }
                    )
                }
            }

            item { ProgramMuSection(
                enrolledCourses = enrolledCourses,
                allCourses = allCourses,
                isLoading = isLoadingEnrollments,
                errorMessage = enrollmentsError,
                onRetry = { sekerjaViewModel.fetchUserEnrollments(currentUser?.id ?: "") },
                searchQuery = searchQuery,
                onCourseClick = { courseId ->
                    navController.navigate("${Screen.CourseSchedule.route}/$courseId")
                }
            ) }
            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}

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
fun SekerjaTopAppBar(
    userName: String?,
    onNotificationClick: () -> Unit,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    TopAppBar(
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
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
                        color = colorResource(id = R.color.magenta_80),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = onNotificationClick) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = colorResource(id = R.color.magenta_80),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                SearchBarUI(
                    value = searchQuery,
                    onValueChange = onSearchQueryChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                )
            }

        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
        ),
        modifier = Modifier
            .height(140.dp)
            .padding(0.dp, 8.dp, 16.dp, 8.dp)
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
fun SectionHeader(title: String, titleColor: Color) {
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
    }
}

@Composable
fun SelesaikanTugasmuSection(
    assignments: List<Assignment>,
    onDetailTugasClick: (courseId: String, assignmentId: String) -> Unit
) {
    Column {
        SectionHeader(
            title = "Selesaikan Tugasmu",
            titleColor = colorResource(id = R.color.magenta_80)
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (assignments.isEmpty()) {
            Text("Tidak ada tugas.", color = Color.Gray)
        } else {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                assignments.forEach { assignment ->
                    TaskCard(
                        icon = Icons.Outlined.Assignment,
                        type = "Assignment",
                        title = assignment.title,
                        onClick = {
                            onDetailTugasClick(assignment.courseId, assignment.assignmentId)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TaskCard(icon: ImageVector, type: String, title: String, onClick: () -> Unit = {}) {
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
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgramMuSection(
    enrolledCourses: List<CourseEnrollment>,
    allCourses: List<Course>,
    isLoading: Boolean,
    errorMessage: String?,
    onRetry: () -> Unit,
    searchQuery: String,
    onCourseClick: (String) -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Semua", "Berjalan", "Selesai")

    val coursesToDisplayBasedOnTab = remember(selectedTabIndex, enrolledCourses) {
        when (selectedTabIndex) {
            1 -> enrolledCourses.filter { !it.completed }
            2 -> enrolledCourses.filter { it.completed }
            else -> enrolledCourses
        }
    }

    val finallyFilteredCourses = remember(searchQuery, coursesToDisplayBasedOnTab, allCourses) {
        if (searchQuery.isBlank()) {
            coursesToDisplayBasedOnTab
        } else {
            coursesToDisplayBasedOnTab.filter { enrollment ->
                val courseDetail = allCourses.find { it.courseId == enrollment.courseId }
                courseDetail?.let {
                    it.title.contains(searchQuery, ignoreCase = true) ||
                            it.company.contains(searchQuery, ignoreCase = true)
                } ?: false
            }
        }
    }

    Column {
        SectionHeader(title = "Program-mu", titleColor = colorResource(id = R.color.magenta_80))

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
            finallyFilteredCourses.isEmpty() -> {
                Text(
                    text = if (searchQuery.isNotBlank()) {
                        "Tidak ada program yang cocok dengan pencarian \"$searchQuery\"."
                    } else {
                        when(selectedTabIndex) {
                            0 -> if (enrolledCourses.isEmpty()) "Anda belum mendaftar program apapun." else "Tidak ada program untuk filter ini."
                            1 -> "Tidak ada program yang sedang berjalan."
                            else -> "Tidak ada program yang sudah selesai."
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
            }
            else -> {
                finallyFilteredCourses.forEach { enrollment ->
                    val courseDetail = allCourses.find { it.courseId == enrollment.courseId }
                    ProgramItemCard(
                        enrollment = enrollment,
                        courseTitle = courseDetail?.title ?: "Judul Tidak Ditemukan",
                        courseCompany = courseDetail?.company ?: "Perusahaan Tidak Diketahui",
                        onClick = { onCourseClick(enrollment.courseId) }
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
    courseTitle: String,
    courseCompany: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
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
                Text(
                    courseCompany,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(id = R.color.magenta_80)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                courseTitle,
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
        SekerjaScreen(navController = rememberNavController())
    }
}