package com.chrisp.setaraapp.feature.sertifikat

import android.util.Log
import com.chrisp.setaraapp.feature.repository.SupabaseInstance
import com.chrisp.setaraapp.feature.sekerja.model.Course
import com.chrisp.setaraapp.feature.sekerja.model.CourseEnrollment
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SertifikatRepository {
    private val supabase = SupabaseInstance.client
    private val tag = "SertifikatRepository"

    suspend fun getCompletedCourses(userId: String): Result<List<Pair<Course, CourseEnrollment>>> {
        return withContext(Dispatchers.IO) {
            try {
                // 1. Ambil semua pendaftaran yang sudah selesai (completed = true) untuk user tertentu
                val completedEnrollments = supabase.postgrest["daftar"]
                    .select {
                        filter {
                            eq("user_id", userId)
                            eq("completed", true)
                        }
                    }.decodeList<CourseEnrollment>()

                if (completedEnrollments.isEmpty()) {
                    return@withContext Result.success(emptyList())
                }

                // 2. Ambil semua ID course dari pendaftaran yang sudah selesai
                val courseIds = completedEnrollments.map { it.courseId }

                // 3. Ambil detail dari semua course tersebut
                val courses = supabase.postgrest["course"]
                    .select {
                        filter {
                            isIn("course_id", courseIds)
                        }
                    }.decodeList<Course>()

                // 4. Gabungkan data Course dengan Enrollment yang sesuai
                val certificateData = completedEnrollments.mapNotNull { enrollment ->
                    courses.find { it.courseId == enrollment.courseId }?.let { course ->
                        Pair(course, enrollment)
                    }
                }

                Log.d(tag, "Successfully fetched ${certificateData.size} completed courses.")
                Result.success(certificateData)

            } catch (e: Exception) {
                Log.e(tag, "Error fetching completed courses: ${e.message}", e)
                Result.failure(e)
            }
        }
    }
}