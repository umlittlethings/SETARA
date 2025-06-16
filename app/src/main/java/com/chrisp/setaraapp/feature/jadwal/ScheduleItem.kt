package com.chrisp.setaraapp.feature.jadwal

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LaptopChromebook
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chrisp.setaraapp.feature.jadwal.model.Schedule
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import com.chrisp.setaraapp.R

/**
 * Helper function untuk memformat rentang waktu dengan lebih informatif.
 */
private fun formatScheduleTime(startTimeStr: String, endTimeStr: String): String {
    return try {
        // Parse ISO string ke Instant
        val startInstant = Instant.parse(startTimeStr)
        val endInstant = Instant.parse(endTimeStr)

        // Konversi ke zona waktu sistem atau zona waktu spesifik (misal, Asia/Jakarta)
        val timeZone = TimeZone.currentSystemDefault() // atau TimeZone.of("Asia/Jakarta")
        val localStartTime = startInstant.toLocalDateTime(timeZone)
        val localEndTime = endInstant.toLocalDateTime(timeZone)

        // Format waktu ke "HH:mm"
        val timeFormatter: (kotlinx.datetime.LocalDateTime) -> String = {
            val hour = it.hour.toString().padStart(2, '0')
            val minute = it.minute.toString().padStart(2, '0')
            "$hour:$minute"
        }

        // Tentukan singkatan zona waktu
        val timezoneAbbreviation = when (timeZone.id) {
            "Asia/Jakarta" -> "WIB"
            "Asia/Makassar" -> "WITA"
            "Asia/Jayapura" -> "WIT"
            else -> "" // Biarkan kosong jika tidak dikenali
        }.let { if (it.isNotEmpty()) " $it" else "" }

        "${timeFormatter(localStartTime)} - ${timeFormatter(localEndTime)}$timezoneAbbreviation"
    } catch (e: Exception) {
        // Fallback jika parsing gagal
        "${startTimeStr.take(5)} - ${endTimeStr.take(5)}"
    }
}


@Composable
fun ScheduleItem(schedule: Schedule) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.gray_20))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Judul Hari
            Text(
                text = schedule.day,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // Detail Jadwal
            InfoRow(
                icon = Icons.Default.Schedule,
                text = formatScheduleTime(schedule.startTime, schedule.endTime)
            )

            InfoRow(
                icon = if (schedule.method.equals("Online", ignoreCase = true))
                    Icons.Default.LaptopChromebook else Icons.Default.LocationOn,
                text = schedule.method
            )

            // Tampilkan lokasi hanya jika tidak kosong
            if (!schedule.location.isNullOrBlank()) {
                InfoRow(
                    icon = Icons.Default.LocationOn,
                    text = schedule.location
                )
            }
        }
    }
}

@Composable
private fun InfoRow(icon: ImageVector, text: String, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 15.sp,
            color = Color.DarkGray
        )
    }
}