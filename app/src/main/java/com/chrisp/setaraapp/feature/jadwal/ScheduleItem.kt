package com.chrisp.setaraapp.feature.jadwal
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.chrisp.setaraapp.feature.jadwal.model.Schedule
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun ScheduleItem(schedule: Schedule) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Baris Hari dan Waktu
            Row(verticalAlignment = Alignment.CenterVertically) {
                InfoChip(
                    icon = Icons.Default.CalendarToday,
                    text = schedule.day,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                val formattedTime = formatTimeRange(schedule.startTime, schedule.endTime)
                InfoChip(
                    icon = Icons.Default.Schedule,
                    text = formattedTime,
                    modifier = Modifier.weight(1.5f)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            // Baris Metode dan Lokasi
            val methodIcon = if (schedule.method.equals("Online", ignoreCase = true)) {
                Icons.Default.LaptopChromebook
            } else {
                Icons.Default.LocationOn
            }
            InfoChip(
                icon = methodIcon,
                text = "${schedule.method}: ${schedule.location ?: "N/A"}"
            )
        }
    }
}

@Composable
private fun InfoChip(icon: ImageVector, text: String, modifier: Modifier = Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}

// Helper untuk format waktu
private fun formatTimeRange(startTimeStr: String, endTimeStr: String): String {
    val parser = DateTimeFormatter.ofPattern("HH:mm:ssXXX")
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return try {
        val startTime = LocalTime.parse(startTimeStr, parser)
        val endTime = LocalTime.parse(endTimeStr, parser)
        "${startTime.format(formatter)} - ${endTime.format(formatter)}"
    } catch (e: Exception) {
        "$startTimeStr - $endTimeStr" // Fallback
    }
}