package com.chrisp.setaraapp.feature.sekerja.detailProgram

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.chrisp.setaraapp.R

@Composable
fun EnrollmentSuccessScreen(onSelesaiClick: () -> Unit) {
    val context = LocalContext.current
    val whatsappLink = "https://chat.whatsapp.com/F39jEbQosiKH1dSrvfe"

    Scaffold(
        bottomBar = {
            Button(
                onClick = onSelesaiClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.magenta_80)
                )
            ) {
                Text(
                    text = "Selesai",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Agar konten bisa di-scroll jika melebihi layar
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(0.5f)) // Memberi ruang di atas

            // Ganti dengan painterResource jika Anda punya aset gambar spesifik
            // Untuk sekarang, menggunakan Icon sebagai placeholder
            Image(
                painter = painterResource(id = R.drawable.ic_selesai_enroll),
                contentDescription = "Pendaftaran Berhasil",
                modifier = Modifier
                    .size(290.dp) // Sesuaikan ukuran gambar
                    .padding(bottom = 32.dp)
            )

            Text(
                text = "Selamat, kamu telah berhasil mendaftar program.",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            val annotatedText = buildAnnotatedString {
                append("Silahkan klik tuatan berikut untuk bergabung ke grup WhatsApp resmi program.\n")
                pushStringAnnotation(tag = "LINK", annotation = whatsappLink) // Tandai bagian link
                withStyle(
                    style = SpanStyle(
                        color = colorResource(id = R.color.magenta_80), // Warna link
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append("\nGabung Grup WhatsApp") // Teks yang lebih deskriptif untuk link
                }
                pop()
            }

            ClickableText(
                text = annotatedText,
                onClick = { offset ->
                    annotatedText.getStringAnnotations(tag = "LINK", start = offset, end = offset)
                        .firstOrNull()?.let { annotation ->
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                // Handle error jika WhatsApp tidak terinstall atau link salah
                                // Misalnya, tampilkan Toast
                            }
                        }
                },
                style = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.weight(1f)) // Memberi ruang di bawah sebelum tombol
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun EnrollmentSuccessScreenPreview() {
    MaterialTheme { // Pastikan Anda membungkus Preview dengan Theme jika menggunakan MaterialTheme colors/typography
        EnrollmentSuccessScreen() {}
    }
}