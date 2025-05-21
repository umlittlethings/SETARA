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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chrisp.setaraapp.R
import com.chrisp.setaraapp.feature.sekerja.model.Course

val programStagesData = listOf(
    ProgramStage("Job Connector Bootcamp", "Raih keahlian yang akan selalu dibutuhkan industri dalam keadaan apapun."),
    ProgramStage("Final Project", "Ciptakan pengalaman nyata dan bangun portofolio dengan real-client project di Final Project Bootcamp."),
    ProgramStage("SETARA Career Network", "Mulai dari membangun CV, persiapan interview hingga konsultasi karir, SETARA siap bikin kamu auto-dilirik!"),
    ProgramStage("SETARA Career Network", "Mulai dari membangun CV, persiapan interview hingga konsultasi karir, SETARA siap bikin kamu auto-dilirik!"), // Duplicated as per image
    ProgramStage("Lifetime Career Support", "Dapatkan fasilitas career support seumur hidup ke 100+ hiring partner yang tersebar di Indonesia.")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailProgramScreen(
    onEnrollmentSuccess: () -> Unit,
    viewModel: DetailProgramViewModel = viewModel(),
    courseId: Course,
) {
    val curriculumModules = remember(courseId.modules) {
        courseId.modules.map { module ->
            CurriculumModule(
                id = module.moduleId,
                title = module.title,
                sessionInfo = "${module.sessionInfo ?: ""}x Sesi",
                topics = module.topics ?: emptyList(),
                isExpanded = module.isExpanded
            )
        }
    }

    var modulesState by remember { mutableStateOf(curriculumModules) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            CourseBottomBar {
                viewModel.enrollToCourse(courseId.courseId)
                onEnrollmentSuccess()
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            item { CourseHeader(
                courseTitle = courseId.title,
                courseCompany = courseId.company,
            ) }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item {
                ProgramSection(
                    description = courseId.detail,
                    periode = courseId.periode,
                    onSeeStagesClick = { showSheet = true }
                )
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

@Composable
fun CourseHeader(
    courseTitle: String,
    courseCompany: String,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Course Header Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = courseTitle,
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = courseCompany,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = Color.White.copy(alpha = 0.8f)
                )
            )
        }
    }
}

@Composable
fun ProgramSection(
    description: String,
    periode: String,
    onSeeStagesClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "PROGRAM",
            style = MaterialTheme.typography.labelMedium.copy(color = colorResource(R.color.green), fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "TANGGAL PELAKSANAAN",
            style = MaterialTheme.typography.labelMedium.copy(color = colorResource(R.color.green), fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = periode,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedButton(
            onClick = onSeeStagesClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = colorResource(R.color.magenta_80)),
            border = ButtonDefaults.outlinedButtonBorder.copy(brush = SolidColor(colorResource(R.color.magenta_80)))
        ) {
            Text("Lihat Tahapan Program")
        }
    }
}

@Composable
fun CurriculumSection(
    modules: List<CurriculumModule>,
    onModuleClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(colorResource(R.color.gray_20), RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "KURIKULUM",
            style = MaterialTheme.typography.labelMedium.copy(color = colorResource(R.color.green), fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 12.dp)
        )
        modules.forEach { module ->
            CurriculumModuleItem(module = module, onClick = { onModuleClick(module.id) })
            if (module != modules.last()) {
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
                tint = colorResource(R.color.magenta_80)
            )
        }
        AnimatedVisibility(visible = module.isExpanded) {
            Column(modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 8.dp)) {
                Text(
                    text = module.sessionInfo,
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .background(colorResource(R.color.green), RoundedCornerShape(4.dp))
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
            style = MaterialTheme.typography.labelMedium.copy(color = colorResource(R.color.green), fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlatformLogo(R.drawable.ic_launcher_foreground, "React Logo")
            PlatformLogo(R.drawable.ic_launcher_foreground, "Node.js Logo")
            PlatformLogo(R.drawable.ic_launcher_foreground, "GitHub Logo")
            PlatformLogo(R.drawable.ic_launcher_foreground, "MySQL Logo")
        }
    }
}

@Composable
fun PlatformLogo(drawableRes: Int, contentDescription: String) {
    Image(
        painter = painterResource(id = drawableRes),
        contentDescription = contentDescription,
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
    )
}

@Composable
fun CourseBottomBar(onDaftarClick: () -> Unit) {
    Surface(shadowElevation = 8.dp, color = Color.White) {
        Button(
            onClick = onDaftarClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.magenta_80))
        ) {
            Text(
                "Daftar",
                style = MaterialTheme.typography.titleMedium.copy(color = Color.White, fontWeight = FontWeight.Bold)
            )
        }
    }
}

val TimelineCircleColor = Color(0xFFE0E0E0)

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
            .height(IntrinsicSize.Min)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .weight(1f)
                    .background(if (isFirst) Color.Transparent else timelineLineColor)
            )
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(circleColor)
            )
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .weight(1f)
                    .background(if (isLast) Color.Transparent else timelineLineColor)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp, horizontal = 8.dp)
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
            .navigationBarsPadding()
            .padding(top = 16.dp, bottom = 24.dp)
    ) {
        Text(
            text = "Tahapan Program",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = SheetTextGreen,
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
                contentPadding = PaddingValues(horizontal = 24.dp),
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

@Preview(showBackground = true)
@Composable
fun CurriculumSectionPreview() {
    MaterialTheme {
        CurriculumSection(
            modules = listOf(
                CurriculumModule("1", "Module 1", "3x Sesi", listOf("Topic 1", "Topic 2"), false),
                CurriculumModule("2", "Module 2", "2x Sesi", listOf("Topic 3", "Topic 4"), true)
            ),
            onModuleClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PlatformCoveredPreview() {
    MaterialTheme {
        PlatformCoveredSection()
    }
}