package com.chrisp.setaraapp.feature.sekerja

import android.util.Log
import com.chrisp.setaraapp.feature.repository.SupabaseInstance
import com.chrisp.setaraapp.feature.sekerja.detailTugas.model.Assignment
import com.chrisp.setaraapp.feature.sekerja.detailTugas.model.Submission
import com.chrisp.setaraapp.feature.sekerja.model.CourseEnrollment
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.io.InputStream
import java.util.UUID

interface SekerjaRepository {
    fun getUserEnrollments(userId: String): Flow<Result<List<CourseEnrollment>>>
    fun isUserEnrolled(userId: String, courseId: String): Flow<Boolean>
    fun getAssignmentsForCourse(courseId: String): Flow<Result<List<Assignment>>>
    suspend fun joinCourse(userId: String, courseId: String): Result<Unit>
    suspend fun createInitialSubmissions(userId: UUID, courseId: String): Result<Unit>
    fun generateMissingSubmissionsForUser(userId: String): Flow<Result<Unit>>

}

class SekerjaRepositoryImpl : SekerjaRepository {

    private val supabase = SupabaseInstance.client
    private val TAG = "SekerjaRepository"
    private val ENROLLMENT_TABLE = "daftar"
    private val ASSIGNMENTS_TABLE = "assignments"
    private val SUBMISSIONS_TABLE = "submissions"
    private val ASSIGNMENT_FILES_BUCKET = "submission"


    override fun getUserEnrollments(userId: String): Flow<Result<List<CourseEnrollment>>> = flow {
        try {
            val enrollments = supabase.postgrest[ENROLLMENT_TABLE]
                .select {
                    filter { eq("user_id", userId) }
                }
                .decodeList<CourseEnrollment>()
            emit(Result.success(enrollments))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun generateMissingSubmissionsForUser(userId: String): Flow<Result<Unit>> = flow {
        try {
            // 1. Ambil semua course yang diikuti user
            val enrollments = supabase.postgrest["daftar"]
                .select { filter { eq("user_id", userId) } }
                .decodeList<CourseEnrollment>()

            // 2. Ambil semua assignment dari semua course tersebut
            val allAssignments = mutableListOf<Assignment>()
            for (enrollment in enrollments) {
                val assignments = supabase.postgrest["assignments"]
                    .select { filter { eq("course_id", enrollment.courseId) } }
                    .decodeList<Assignment>()
                allAssignments.addAll(assignments)
            }

            // 3. Cek submission yang sudah ada
            val existingSubmissions = supabase.postgrest["tugas_submissions"]
                .select {
                    filter { eq("user_id", userId) }
                }.decodeList<Submission>()

            val existingAssignmentIds =
                existingSubmissions.map { it.assignmentId.toString() }.toSet()

            // 4. Filter assignment yang belum punya submission
            val missingAssignments = allAssignments.filter {
                !existingAssignmentIds.contains(it.assignmentId)
            }

            // 5. Insert submission dummy untuk assignment yang belum punya
            for (assignment in missingAssignments) {
                val newSubmission = mapOf(
                    "submission_id" to UUID.randomUUID(),
                    "assignment_id" to UUID.fromString(assignment.assignmentId),
                    "user_id" to UUID.fromString(userId),
                    "is_submitted" to false,
                    "created_at" to Clock.System.now().toString()
                )

                supabase.postgrest["tugas_submissions"].insert(newSubmission)
            }

            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }


    override fun isUserEnrolled(userId: String, courseId: String): Flow<Boolean> = flow {
        val result = supabase.postgrest[ENROLLMENT_TABLE]
            .select {
                filter {
                    eq("user_id", userId)
                    eq("course_id", courseId)
                }
                limit(1)
            }
            .decodeList<CourseEnrollment>()
        emit(result.isNotEmpty())
    }

    override fun getAssignmentsForCourse(courseId: String): Flow<Result<List<Assignment>>> = flow {
        try {
            val assignments = supabase.postgrest[ASSIGNMENTS_TABLE]
                .select {
                    filter { eq("course_id", courseId) }
                }
                .decodeList<Assignment>()
            emit(Result.success(assignments))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun joinCourse(userId: String, courseId: String): Result<Unit> {
        return try {
            val data = mapOf(
                "user_id" to userId,
                "course_id" to courseId
            )
            supabase.postgrest["daftar"].insert(data)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createInitialSubmissions(userId: UUID, courseId: String): Result<Unit> {
        return try {
            val assignments = supabase.postgrest["assignments"]
                .select {
                    filter { eq("course_id", courseId) }
                }
                .decodeList<Assignment>()

            for (assignment in assignments) {
                val data = mapOf(
                    "submission_id" to UUID.randomUUID(),
                    "user_id" to userId,
                    "assignment_id" to UUID.fromString(assignment.assignmentId),
                    "is_submitted" to false,
                    "created_at" to kotlinx.datetime.Clock.System.now().toString()
                )
                supabase.postgrest["tugas_submissions"].insert(data)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}



