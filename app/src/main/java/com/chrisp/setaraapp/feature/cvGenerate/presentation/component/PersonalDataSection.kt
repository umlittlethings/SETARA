package com.chrisp.setaraapp.feature.cvGenerate.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
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
import com.chrisp.setaraapp.component.CustomIconTextFieldUI
import com.chrisp.setaraapp.component.LongTextFieldUI
import com.chrisp.setaraapp.component.TextFieldUI
import com.chrisp.setaraapp.feature.cvGenerate.presentation.FormViewModel
import com.chrisp.setaraapp.R

@Composable
fun PersonalDataSection(viewModel: FormViewModel) {
    val fullName = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val linkedin = remember { mutableStateOf("") }
    val phone = remember { mutableStateOf("") }
    val address = remember { mutableStateOf("") }
    val summary = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Isilah data diri di bawah ini dengan lengkap dan benar",
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Nama Lengkap",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            TextFieldUI(
                value = fullName.value,
                onValueChange = {
                    fullName.value = it
                    viewModel.updateFullName(it)
                },
                placeholder = "Masukkan nama lengkap",
                leadingIcon = Icons.Default.Person,
                keyboardType = KeyboardType.Text
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Email",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            TextFieldUI(
                value = email.value,
                onValueChange = {
                    email.value = it
                    viewModel.updateEmail(it)
                },
                placeholder = "Masukkan email",
                leadingIcon = Icons.Default.Email,
                keyboardType = KeyboardType.Text
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Link Linkedin",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            CustomIconTextFieldUI(
                value = linkedin.value,
                onValueChange = {
                    linkedin.value = it
                    viewModel.updateLinkedin(it)
                },
                placeholder = "Masukkan link linkedin",
                leadingIcon = painterResource(R.drawable.ic_linkedin),
                keyboardType = KeyboardType.Text,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Nomer Telepon",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            TextFieldUI(
                value = phone.value,
                onValueChange = {
                    phone.value = it
                    viewModel.updatePhone(it)
                },
                placeholder = "Masukkan nomor telepon",
                leadingIcon = Icons.Default.Phone,
                keyboardType = KeyboardType.Phone
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Asal Kota",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            TextFieldUI(
                value = address.value,
                onValueChange = {
                    address.value = it
                    viewModel.updateAddress(it)
                },
                placeholder = "Masukkan asal kota",
                leadingIcon = Icons.Default.Place,
                keyboardType = KeyboardType.Text
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Deskripsikan Dirimu",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            LongTextFieldUI(
                value = summary.value,
                onValueChange = {
                    summary.value = it
                    viewModel.updateSummary(it)
                },
                placeholder = "Deskripsikan tentang dirimu",
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
    PersonalDataSection(
        viewModel = FormViewModel()
    )
}