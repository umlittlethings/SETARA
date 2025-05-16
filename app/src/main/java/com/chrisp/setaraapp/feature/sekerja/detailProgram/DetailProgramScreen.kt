package com.chrisp.setaraapp.feature.sekerja.detailProgram

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.chrisp.setaraapp.R

// --- Dummy Data ---
data class CurriculumModule(
    val id: Int,
    val title: String,
    val sessionInfo: String,
    val topics: List<String>,
    var isExpanded: Boolean = false
)

val sampleModules = listOf(
    CurriculumModule(
        id = 1,
        title = "Modul 01: Programming Fundamental",
        sessionInfo = "14x Sesi",
        topics = listOf(
            "Intro to programming, variables and data types",
            "Conditional and loop statements",
            "Intro to Git, Github & Exercise",
            "Array and function",
            "Object oriented programming",
            "Data structure",
            "Algorithm",
            "+ Exam",
            "+ Code Challenge"
        )
    ),
    CurriculumModule(id = 2, title = "Modul 02: Front End Web Development", sessionInfo = "10x Sesi", topics = listOf("HTML", "CSS", "JavaScript")),
    CurriculumModule(id = 3, title = "Modul 03: Back End Web Development", sessionInfo = "12x Sesi", topics = listOf("Node.js", "Express", "Databases")),
    CurriculumModule(id = 4, title = "Modul 04: Advance Software Development", sessionInfo = "8x Sesi", topics = listOf("CI/CD", "Testing", "Deployment")),
    CurriculumModule(id = 5, title = "Modul 05: Final Project Bootcamp", sessionInfo = "6x Sesi", topics = listOf("Project Planning", "Development", "Presentation"))
)

// --- Dummy Data for Program Stages ---
data class ProgramStage(val title: String, val description: String)
val programStagesData = listOf(
    ProgramStage("Job Connector Bootcamp", "Raih keahlian yang akan selalu dibutuhkan industri dalam keadaan apapun."),
    ProgramStage("Final Project", "Ciptakan pengalaman nyata dan bangun portofolio dengan real-client project di Final Project Bootcamp."),
    ProgramStage("SETARA Career Network", "Mulai dari membangun CV, persiapan interview hingga konsultasi karir, SETARA siap bikin kamu auto-dilirik!"),
    ProgramStage("SETARA Career Network", "Mulai dari membangun CV, persiapan interview hingga konsultasi karir, SETARA siap bikin kamu auto-dilirik!"), // Duplicated as per image
    ProgramStage("Lifetime Career Support", "Dapatkan fasilitas career support seumur hidup ke 100+ hiring partner yang tersebar di Indonesia.")
)

// --- Colors ---
val PrimaryPurple = Color(0xFF6A0DAD)
val AccentPink = Color(0xFFE91E63)
val TextGreen = Color(0xFF4CAF50)
val LightGrayBackground = Color(0xFFF5F5F5)
val DarkerPurpleButton = Color(0xFF880E4F)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailProgramScreen(navController: NavController) {
    var modulesState by remember { mutableStateOf(sampleModules) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }

    Box {
        Scaffold(
            bottomBar = {
                CourseBottomBar {
                    // Handle Daftar click
                }
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                item { CourseHeader() }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item {
                    ProgramSection(onSeeStagesClick = { showSheet = true })
                }
                item { Spacer(modifier = Modifier.height(24.dp)) }
                item {
                    CurriculumSection(
                        modules = modulesState,
                        onModuleClick = { moduleId ->
                            modulesState = modulesState.map {
                                if (it.id == moduleId) it.copy(isExpanded = !it.isExpanded) else it
                            }
                        }
                    )
                }
                item { Spacer(modifier = Modifier.height(24.dp)) }
                item { PlatformCoveredSection() }
                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }

        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                sheetState = sheetState,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            ) {
                ProgramStagesSheetContent(
                    stages = programStagesData
                )
            }
        }
    }
}

@Composable
fun CourseHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Adjust height as needed
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background), // Replace with your actual header image
            contentDescription = "Course Header Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)) // Semi-transparent overlay
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom, // Align text to bottom
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Fullstack Web Developer",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Job Connector Bootcamp",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = Color.White.copy(alpha = 0.8f)
                )
            )
        }
    }
}

@Composable
fun ProgramSection(onSeeStagesClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "PROGRAM",
            style = MaterialTheme.typography.labelMedium.copy(color = TextGreen, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            buildAnnotatedString {
                append("Raih karir sebagai Programmer dalam ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("13 minggu!")
                }
            },
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "UI Designer memiliki tugas menentukan tampilan aplikasi atau situs. Sementara UX Designer menentukan bagaimana suatu aplikasi atau situs bisa beroperasi dengan mudah. Namun dalam bekerja, keduanya harus berlandaskan pada hasil riset supaya aplikasi atau situs yang dirancang benar-benar efektif.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedButton(
            onClick = onSeeStagesClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryPurple),
            border = ButtonDefaults.outlinedButtonBorder.copy(brush = SolidColor(PrimaryPurple))
        ) {
            Text("Lihat Tahapan Program")
        }
    }
}

@Composable
fun CurriculumSection(
    modules: List<CurriculumModule>,
    onModuleClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(LightGrayBackground, RoundedCornerShape(8.dp)) // Card-like background for the whole section
            .padding(16.dp) // Inner padding for the card
    ) {
        Text(
            text = "KURIKULUM",
            style = MaterialTheme.typography.labelMedium.copy(color = TextGreen, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 12.dp)
        )
        modules.forEach { module ->
            CurriculumModuleItem(module = module, onClick = { onModuleClick(module.id) })
            if (module != modules.last()) { // Add divider if not the last item
                Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.LightGray.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
fun CurriculumModuleItem(module: CurriculumModule, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = module.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (module.isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = if (module.isExpanded) "Collapse" else "Expand",
                tint = PrimaryPurple
            )
        }
        AnimatedVisibility(visible = module.isExpanded) {
            Column(modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 8.dp)) {
                Text(
                    text = module.sessionInfo,
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .background(AccentPink, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                module.topics.forEach { topic ->
                    Text(
                        text = "• $topic",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PlatformCoveredSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "PLATFORM COVERED",
            style = MaterialTheme.typography.labelMedium.copy(color = TextGreen, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Replace with your actual logos
            PlatformLogo(R.drawable.ic_launcher_foreground, "React Logo")
            PlatformLogo(R.drawable.ic_launcher_foreground, "Node.js Logo")
            PlatformLogo(R.drawable.ic_launcher_foreground, "GitHub Logo")
            PlatformLogo(R.drawable.ic_launcher_foreground, "MySQL Logo")
            // Add more if needed, consider a LazyRow if many
        }
    }
}

@Composable
fun PlatformLogo(drawableRes: Int, contentDescription: String) {
    Image(
        painter = painterResource(id = drawableRes),
        contentDescription = contentDescription,
        modifier = Modifier
            .size(40.dp) // Adjust size as needed
            .clip(CircleShape) // Optional: if logos are circular or to make them so
    )
}

@Composable
fun CourseBottomBar(onDaftarClick: () -> Unit) {
    Surface(shadowElevation = 8.dp) { // Add shadow to distinguish from content
        Button(
            onClick = onDaftarClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp) // Padding around the button inside the bar
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkerPurpleButton)
        ) {
            Text(
                "Daftar",
                style = MaterialTheme.typography.titleMedium.copy(color = Color.White, fontWeight = FontWeight.Bold)
            )
        }
    }
}

val TimelineCircleColor = Color(0xFFE0E0E0) // Light gray


@Composable
fun ProgramStageTimelineItem(
    stage: ProgramStage,
    isFirst: Boolean,
    isLast: Boolean,
    timelineLineColor: Color,
    circleColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min) // Ensures the Row is as tall as its tallest child (text or timeline graphics)
    ) {
        // Timeline graphics Column (Circle and Lines)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight() // Takes the height determined by the text content on the right
                .padding(horizontal = 8.dp) // Padding around the timeline graphics
        ) {
            // Line above the circle (transparent for the first item)
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .weight(1f) // Takes up proportional space
                    .background(if (isFirst) Color.Transparent else timelineLineColor)
            )

            // Circle
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(circleColor)
            )

            // Line below the circle (transparent for the last item)
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .weight(1f) // Takes up proportional space
                    .background(if (isLast) Color.Transparent else timelineLineColor)
            )
        }

        // Text content Column
        Column(
            modifier = Modifier
                .weight(1f) // Takes remaining width
                .padding(vertical = 8.dp, horizontal = 8.dp) // Padding for text content
        ) {
            Text(
                text = stage.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stage.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ProgramStageTimelineItemPreview() {
    MaterialTheme {
        Column {
            ProgramStageTimelineItem(
                stage = programStagesData[0],
                isFirst = true,
                isLast = false,
                timelineLineColor = Color.Magenta,
                circleColor = Color.LightGray
            )
            ProgramStageTimelineItem(
                stage = programStagesData[1],
                isFirst = false,
                isLast = false,
                timelineLineColor = Color.Magenta,
                circleColor = Color.LightGray
            )
            ProgramStageTimelineItem(
                stage = programStagesData.last(),
                isFirst = false,
                isLast = true,
                timelineLineColor = Color.Magenta,
                circleColor = Color.LightGray
            )
        }
    }
}

// Assuming TextGreen and DarkerPurpleButton are defined in your CourseScreen or accessible
// For this example, let's redefine them or use placeholders if not.
val SheetTextGreen = Color(0xFF4CAF50)
val SheetTimelinePurple = Color(0xFF880E4F)


@Composable
fun ProgramStagesSheetContent(
    stages: List<ProgramStage>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding() // Handles system navigation bar insets
            .padding(top = 16.dp, bottom = 24.dp) // Padding for the sheet content
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Tahapan Program",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = SheetTextGreen, // Use the defined green color
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (stages.isEmpty()) {
            Text(
                "Tidak ada tahapan program yang tersedia.",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 24.dp), // Horizontal padding for list items
            ) {
                itemsIndexed(stages) { index, stage ->
                    ProgramStageTimelineItem(
                        stage = stage,
                        isFirst = index == 0,
                        isLast = index == stages.lastIndex,
                        timelineLineColor = SheetTimelinePurple,
                        circleColor = TimelineCircleColor
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 600)
@Composable
fun ProgramStagesSheetContentPreview() {
    MaterialTheme {
        ProgramStagesSheetContent(stages = programStagesData)
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 200)
@Composable
fun ProgramStagesSheetContentEmptyPreview() {
    MaterialTheme {
        ProgramStagesSheetContent(stages = emptyList())
    }
}


// --- Preview ---
@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun DetailProgramScreenPreview() {
    // Wrap in a MaterialTheme if you have one defined for your app for consistent styling
    MaterialTheme {
        DetailProgramScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun ProgramSectionPreview() {
    MaterialTheme {
        ProgramSection {}
    }
}

@Preview(showBackground = true)
@Composable
fun CurriculumSectionPreview() {
    MaterialTheme {
        var modulesState by remember { mutableStateOf(sampleModules.map { it.copy(isExpanded = it.id == 1) }) }
        CurriculumSection(modules = modulesState, onModuleClick = {moduleId ->
            modulesState = modulesState.map {
                if (it.id == moduleId) it.copy(isExpanded = !it.isExpanded) else it
            }
        })
    }
}

@Preview(showBackground = true)
@Composable
fun PlatformCoveredPreview() {
    MaterialTheme {
        PlatformCoveredSection()
    }
}