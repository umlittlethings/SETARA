package com.chrisp.setaraapp.feature.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.chrisp.setaraapp.navigation.BottomNavigationBar
import com.chrisp.setaraapp.R
import com.chrisp.setaraapp.feature.auth.AuthViewModel

// Define colors (can be moved to a Theme file)
val primaryMagentaDark = Color(0xFF8D1C56) // A darker magenta for text on white, or as seen in image
val primaryMagentaBackground = Color(0xFF880E4F) // Magenta for background
//val primaryMagentaHighlight = Color(0xFFC2185B) // Magenta for highlighted text within hero
val whiteColor = Color.White
val lightGrayText = Color(0xFF757575)
val chipBorderColor = Color.LightGray
val chipTextColor = Color.DarkGray
val filterChipSelectedBackgroundColor = primaryMagentaDark.copy(alpha = 0.1f)
val filterChipSelectedTextColor = primaryMagentaDark
val lightOrangeTagBackground = Color(0xFFFFE0B2) // Lighter orange for "Disabilitas Mental"
val darkTextColor = Color(0xFF333333)
val iconColor = Color(0xFF616161) // General icon color for job details


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel(), // Renamed for clarity
    authViewModel: AuthViewModel = viewModel(), // Get instance of AuthViewModel
    onNavigateToDetail: (String) -> Unit
) {
    // Observe state from HomeViewModel
    val coursesList = homeViewModel.courses
    val isLoadingCourses = homeViewModel.isLoading // Renamed for clarity
    val coursesErrorMessage = homeViewModel.errorMessage // Renamed for clarity

    // Observe user data from AuthViewModel
    val currentUser by authViewModel.currentUser.collectAsState()
    // val isUserLoading by authViewModel.isUserLoading (if you need a specific loader for user data)
    // val userError by authViewModel.userError (if you need to display user-specific errors here)

    // Optional: Trigger profile fetch if not already done and user is null but should be logged in
    // This depends on your app's logic. The ViewModel now tries to fetch on login.
    // LaunchedEffect(key1 = currentUser) {
    //     if (authViewModel.isUserLoggedIn().firstOrNull() == true && currentUser == null) {
    //         authViewModel.fetchCurrentUserProfile()
    //     }
    // }


    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        },
        containerColor = primaryMagentaBackground
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(18.dp))
                HomeTopAppBar(userName = currentUser?.f_name) // Pass f_name or a default
            }
            item {
                HomeSearchBar()
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .background(whiteColor)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    HeroCard()
                    ProgramSection()

                    if (isLoadingCourses) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = primaryMagentaDark)
                            Spacer(modifier = Modifier.height(120.dp))
                        }
                    } else if (coursesErrorMessage != null) {
                        Text(
                            text = coursesErrorMessage,
                            color = Color.Red,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp)
                        )
                        Button(
                            onClick = { homeViewModel.loadCourses() },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("Coba Lagi")
                        }
                    } else if (coursesList.isEmpty()) {
                        Text(
                            text = "Tidak ada program yang tersedia saat ini.",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp),
                            color = lightGrayText
                        )
                    } else {
                        coursesList.forEach { course ->
                            ProgramItemCard(
                                logo = painterResource(id = R.drawable.ic_google),
                                title = course.title,
                                company = course.company,
                                periode = course.periode,
                                disabilityTag = course.disabilityTag,
                                tagColor = lightOrangeTagBackground,
                                onClick = {
                                    onNavigateToDetail(course.courseId)
                                }
                            )
                        }
                    }
                    AlumniSection()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(userName: String?) { // Accept userName as a parameter
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF29B6F6)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        // Display first letter of userName if available, else a default
                        text = userName?.firstOrNull()?.uppercaseChar()?.toString() ?: "U",
                        fontSize = 18.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Selamat Datang!",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = whiteColor
                    )
                    Text(
                        // Display userName if available, else a default or empty
                        text = userName ?: "Pengguna", // Display actual name or a default
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = whiteColor
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = { /* Handle notification click */ }) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    tint = whiteColor,
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSearchBar() {
    var searchQuery by remember { mutableStateOf("") }
    OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp), // Padding around search bar
        placeholder = { Text("Cari program untukmu", color = primaryMagentaBackground.copy(alpha = 0.7f)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search Icon",
                tint = colorResource(id = R.color.magenta_80)
            )
        },
        shape = RoundedCornerShape(8.dp), // Rounded corners for search bar
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent, // No border highlight on focus
            unfocusedIndicatorColor = Color.Transparent, // No border
            disabledIndicatorColor = Color.Transparent,
            focusedContainerColor = whiteColor, // White background
            unfocusedContainerColor = whiteColor,
            cursorColor = primaryMagentaDark,
            focusedLeadingIconColor = primaryMagentaDark,
            unfocusedLeadingIconColor = primaryMagentaDark,
        ),
        singleLine = true
    )
}

@Composable
fun HeroCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.8f)) // Dark overlay as in image
    ) {
        Box(modifier = Modifier.height(180.dp)) { // Fixed height for the hero card
            Image(
                painter = painterResource(id = R.drawable.iklan_homescreen), // Replace with actual hero image
                contentDescription = "Hero Image",
                contentScale = ContentScale.Crop, // Crop to fill
                modifier = Modifier.fillMaxSize()
            )
            // Dark overlay for text readability - already handled by Card containerColor
            // Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)))
//
//            Column(
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .padding(16.dp)
//                    .width(IntrinsicSize.Max), // Constrain width to text
//                verticalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(
//                    text = buildAnnotatedString {
//                        append("Raih ")
//                        withStyle(style = SpanStyle(color = primaryMagentaHighlight, fontWeight = FontWeight.Bold)) {
//                            append("karir")
//                        }
//                        append(" dan\n")
//                        withStyle(style = SpanStyle(color = primaryMagentaHighlight, fontWeight = FontWeight.Bold)) {
//                            append("keahlian digital")
//                        }
//                        append("\ndengan peluang kerja\ntanpa batas!")
//                    },
//                    color = whiteColor,
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Medium,
//                    lineHeight = 24.sp
//                )
//                Text(
//                    text = "SETARA telah menjadi lembaga pendidikan teknologi digital bagi disabilitas sejak 2025.",
//                    color = whiteColor.copy(alpha = 0.9f),
//                    fontSize = 11.sp,
//                    lineHeight = 16.sp
//                )
//            }
        }
    }
}

@Composable
fun ProgramSection() {
    Column {
        Text(
            "Pilihan Program Sekerja ",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = primaryMagentaDark
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item { FilterButtonChip(text = "Filter", icon = Icons.Filled.Tune, isSelected = true) }
            item { FilterButtonChip(text = "Semua") }
            item { FilterButtonChip(text = "Bootcamp") }
            item { FilterButtonChip(text = "Event") }
            item { FilterButtonChip(text = "Workshop") } // Added for scrollability demo
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterButtonChip(text: String, icon: ImageVector? = null, isSelected: Boolean = false) {
    val backgroundColor = if (isSelected) filterChipSelectedBackgroundColor else Color.Transparent
    val contentColor = if (isSelected) filterChipSelectedTextColor else chipTextColor
    val borderColor = if (isSelected) primaryMagentaDark else chipBorderColor

    OutlinedButton(
        onClick = { /* TODO: Handle filter click */ },
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = text, modifier = Modifier.size(18.dp), tint = contentColor)
            Spacer(modifier = Modifier.width(6.dp))
        }
        Text(text, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun ProgramItemCard(
    logo: Painter,
    title: String,
    company: String,
    periode: String,
    disabilityTag: String,
    tagColor: Color,
    onClick: () -> Unit = { /* Handle click */ }
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = whiteColor), // card is on white background itself, so slightly off-white if needed
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // subtle shadow
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top // Align company logo and text block
            ) {
                Image(
                    painter = logo,
                    contentDescription = "$company logo",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray.copy(alpha = 0.3f)) // Placeholder background
                        .padding(4.dp) // If logo needs padding within its box
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = darkTextColor)
                    Text(company, fontSize = 14.sp, color = lightGrayText)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ProgramDetailItem(icon = Icons.Filled.AccessTime, text = periode)
            }
            Spacer(modifier = Modifier.height(10.dp))
            DisabilityTag(text = disabilityTag, backgroundColor = tagColor)
        }
    }
}

@Composable
fun ProgramDetailItem(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = text, tint = iconColor, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text, fontSize = 13.sp, color = darkTextColor)
    }
}

@Composable
fun DisabilityTag(text: String, backgroundColor: Color) {
    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp) // Pill shape
    ) {
        Text(
            text,
            color = darkTextColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun AlumniSection() {
    val companyLogos = listOf(
        painterResource(id = R.drawable.ic_google) to "Astra",
        painterResource(id = R.drawable.ic_google) to "Unilever",
        painterResource(id = R.drawable.ic_google) to "Goto",
        painterResource(id = R.drawable.ic_google) to "Telkomsel",
        // Add more if they scroll off-screen in the image
    )
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            buildAnnotatedString {
                append("Alumni ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = primaryMagentaDark)) {
                    append("SETARA")
                }
                append(" telah bekerja di perusahaan ini")
            },
            fontSize = 14.sp,
            color = darkTextColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround, // Distribute space
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(companyLogos.size) { index ->
                Image(
                    painter = companyLogos[index].first,
                    contentDescription = companyLogos[index].second,
                    modifier = Modifier
                        .height(30.dp) // Adjust size as needed
                        .padding(horizontal = 8.dp), // Space between logos
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen(navController = rememberNavController(), onNavigateToDetail = {})
    }
}