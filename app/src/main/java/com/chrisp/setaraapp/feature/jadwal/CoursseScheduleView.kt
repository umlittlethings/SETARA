package com.chrisp.setaraapp.feature.jadwal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chrisp.setaraapp.feature.jadwal.model.Schedule

@Composable
fun CourseScheduleView(schedules: List<Schedule>) {
    Column {
        Text(
            text = "Jadwal Kelas",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.height(32.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (schedules.isEmpty()) {
            Text("Jadwal untuk kursus ini belum tersedia.")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(schedules) { schedule ->
                    ScheduleItem(schedule = schedule)
                }
            }
        }
    }
}