package com.chrisp.setaraapp.feature.cvGenerate.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chrisp.setaraapp.R
import com.chrisp.setaraapp.component.CustomIconTextFieldUI
import com.chrisp.setaraapp.component.LongTextFieldUI
import com.chrisp.setaraapp.component.TextFieldUI
import com.chrisp.setaraapp.feature.cvGenerate.presentation.FormViewModel

@Composable
fun WorkExperienceDataSection(viewModel: FormViewModel) {
    val position = remember { mutableStateOf("") }
    val company = remember { mutableStateOf("") }
    val startDate = remember { mutableStateOf("") }
    val endDate = remember { mutableStateOf("") }
    val workDescription = remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Isilah riwayat pengalaman kerja Anda di bawah ini dengan lengkap dan benar",
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Posisi",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            CustomIconTextFieldUI(
                value = position.value,
                onValueChange = {
                    position.value = it
                    viewModel.updatePosition(it)
                },
                placeholder = "Mobile Developer",
                leadingIcon = painterResource(R.drawable.ic_sekerja),
                keyboardType = KeyboardType.Text,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Nama Perusahaan",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            CustomIconTextFieldUI(
                value = company.value,
                onValueChange = {
                    company.value = it
                    viewModel.updateCompany(it)
                },
                placeholder = "PT Setara Jaya",
                leadingIcon = painterResource(R.drawable.ic_building),
                keyboardType = KeyboardType.Text,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tanggal Mulai",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            TextFieldUI(
                value = startDate.value,
                onValueChange = {
                    startDate.value = it
                    viewModel.updateStartDate(it)
                },
                placeholder = "Agu 2025",
                leadingIcon = Icons.Default.DateRange,
                keyboardType = KeyboardType.Text
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tanggal Selesai",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            TextFieldUI(
                value = endDate.value,
                onValueChange = {
                    endDate.value = it
                    viewModel.updateEndDate(it)
                },
                placeholder = "Des 2029",
                leadingIcon = Icons.Default.DateRange,
                keyboardType = KeyboardType.Text
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Deskripsi Pekerjaan",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            LongTextFieldUI(
                value = workDescription.value,
                onValueChange = {
                    workDescription.value = it
                    viewModel.updateWorkDescription(it)
                },
                placeholder = "Deskripsikan pekerjaan anda",
                keyboardType = KeyboardType.Text,
                visualTransformation = VisualTransformation.None,
                textColor = colorResource(id = R.color.magenta_80),
                borderColor = colorResource(id = R.color.borderPink),
                placeholderColor = colorResource(id = R.color.borderPink),
            )
        }
    }
}

@Preview
@Composable
private fun RegisterPrev() {
    WorkExperienceDataSection(
        viewModel = FormViewModel()
    )
}