package com.chrisp.setaraapp.feature.sekerja

import android.util.Log
import com.chrisp.setaraapp.feature.repository.SupabaseInstance
import com.chrisp.setaraapp.feature.sekerja.model.CourseEnrollment
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface SekerjaRepository {
    fun getUserEnrollments(userId: String): Flow<Result<List<CourseEnrollment>>>
}

class SekerjaRepositoryImpl : SekerjaRepository {

    private val supabase = SupabaseInstance.client
    private val TAG = "SekerjaRepository"
    private val ENROLLMENT_TABLE = "daftar" // Ganti dengan nama tabel Anda ("daftar")

    override fun getUserEnrollments(userId: String): Flow<Result<List<CourseEnrollment>>> = flow {
        try {
            Log.d(TAG, "Fetching enrollments for userId: $userId from table: $ENROLLMENT_TABLE")
            val enrollments = supabase.postgrest[ENROLLMENT_TABLE]
                .select {
                    filter {
                        eq("user_id", userId) // Filter langsung di query Supabase
                    }
                }
                .decodeList<CourseEnrollment>()
            Log.d(TAG, "Successfully fetched ${enrollments.size} enrollments for userId: $userId")
            enrollments.forEach { Log.d(TAG, "Enrollment: $it") }
            emit(Result.success(enrollments))
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load enrollments for userId: $userId", e)
            emit(Result.failure(e))
        }
    }

    // Implementasi fungsi lain jika ada
    // override fun enrollUserInCourse(...) { ... }
}