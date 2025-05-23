package com.chrisp.setaraapp.feature.sekerja

import android.util.Log
import com.chrisp.setaraapp.feature.repository.SupabaseInstance
import com.chrisp.setaraapp.feature.sekerja.model.CourseEnrollment
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface SekerjaRepository {
    fun getUserEnrollments(userId: String): Flow<Result<List<CourseEnrollment>>>
    // Ubah tipe kembalian agar tidak eksplisit Result di sini, biarkan ViewModel yang membungkusnya
    fun isUserEnrolled(userId: String, courseId: String): Flow<Boolean>
}

class SekerjaRepositoryImpl : SekerjaRepository {

    private val supabase = SupabaseInstance.client
    private val TAG = "SekerjaRepository"
    private val ENROLLMENT_TABLE = "daftar"

    override fun getUserEnrollments(userId: String): Flow<Result<List<CourseEnrollment>>> = flow {
        try {
            Log.d(TAG, "Fetching enrollments for userId: $userId from table: $ENROLLMENT_TABLE")
            val enrollments = supabase.postgrest[ENROLLMENT_TABLE]
                .select {
                    filter {
                        eq("user_id", userId)
                    }
                }
                .decodeList<CourseEnrollment>()
            Log.d(TAG, "Successfully fetched ${enrollments.size} enrollments for userId: $userId")
            enrollments.forEach { Log.d(TAG, "Enrollment: $it") }
            emit(Result.success(enrollments))
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load enrollments for userId: $userId", e)
            emit(Result.failure(e)) // Ini boleh karena tipe return adalah Flow<Result<...>>
        }
    }

    override fun isUserEnrolled(userId: String, courseId: String): Flow<Boolean> = flow {
        // Hilangkan try-catch di sini, biarkan exception dilempar jika ada
        Log.d(TAG, "Checking enrollment for userId: $userId, courseId: $courseId from table: $ENROLLMENT_TABLE")
        val result = supabase.postgrest[ENROLLMENT_TABLE]
            .select {
                filter {
                    eq("user_id", userId)
                    eq("course_id", courseId)
                }
                limit(1)
            }
            .decodeList<CourseEnrollment>()

        val isEnrolled = result.isNotEmpty()
        Log.d(TAG, "User enrolled: $isEnrolled for userId: $userId, courseId: $courseId. Result size: ${result.size}")
        emit(isEnrolled) // Langsung emit boolean
    }
}