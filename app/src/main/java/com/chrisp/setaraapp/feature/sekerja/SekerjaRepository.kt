package com.chrisp.setaraapp.feature.sekerja

import android.util.Log
import com.chrisp.setaraapp.feature.repository.SupabaseInstance
import com.chrisp.setaraapp.feature.sekerja.detailTugas.model.Assignment
import com.chrisp.setaraapp.feature.sekerja.detailTugas.model.Submission
import com.chrisp.setaraapp.feature.sekerja.model.CourseEnrollment
import com.chrisp.setaraapp.feature.jadwal.model.Schedule
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import java.io.InputStream
import java.util.UUID

// Add serializable data classes for database operations
@Serializable
data class EnrollmentData(
    @SerialName("user_id") val userId: String,
    @SerialName("course_id") val courseId: String
)

@Serializable
data class SubmissionData(
    @SerialName("submission_id") val submissionId: String,
    @SerialName("user_id") val userId: String,
    @SerialName("assignment_id") val assignmentId: String,
    @SerialName("is_submitted") val isSubmitted: Boolean,
    @SerialName("created_at") val createdAt: String,
    @SerialName("file_path") val fileUrl: String? = null,
    @SerialName("file_name") val fileName: String? = null,
    @SerialName("submitted_at") val submittedAt: String? = null
)

@Serializable
data class SubmissionUpdateData(
    @SerialName("file_path") val fileUrl: String,
    @SerialName("file_name") val fileName: String,
    @SerialName("is_submitted") val isSubmitted: Boolean,
    @SerialName("submitted_at") val submittedAt: String
)

interface SekerjaRepository {
    fun getUserEnrollments(userId: String): Flow<Result<List<CourseEnrollment>>>
    fun isUserEnrolled(userId: String, courseId: String): Flow<Boolean>
    fun getAssignmentsForCourse(courseId: String): Flow<Result<List<Assignment>>>
    suspend fun joinCourse(userId: String, courseId: String): Result<Unit>
    suspend fun createInitialSubmissions(userId: UUID, courseId: String): Result<Unit>
    fun generateMissingSubmissionsForUser(userId: String): Flow<Result<Unit>>
    fun getSchedulesForCourse(courseId: String): Flow<Result<List<Schedule>>>
    // New method for file submission
    fun submitAssignmentWithFile(
        userId: String,
        assignmentId: String,
        fileInputStream: InputStream,
        fileName: String
    ): Flow<Result<Unit>>
}

class SekerjaRepositoryImpl : SekerjaRepository {

    private val supabase = SupabaseInstance.client
    private val TAG = "SekerjaRepository"
    private val ENROLLMENT_TABLE = "daftar"
    private val ASSIGNMENTS_TABLE = "assignments"
    private val SUBMISSIONS_TABLE = "submissions"
    private val ASSIGNMENT_FILES_BUCKET = "submission"
    private val SCHEDULES_TABLE = "schedules"

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

    override fun getSchedulesForCourse(courseId: String): Flow<Result<List<Schedule>>> = flow {
        try {
            val schedules = supabase.postgrest[SCHEDULES_TABLE]
                .select {
                    filter { eq("course_id", courseId) }
                }
                .decodeList<Schedule>()
            emit(Result.success(schedules))
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching schedules for course $courseId", e)
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
            val existingSubmissions = supabase.postgrest["submissions"]
                .select {
                    filter { eq("user_id", userId) }
                }.decodeList<Submission>()

            val existingAssignmentIds =
                existingSubmissions.map { it.assignmentId }.toSet()

            // 4. Filter assignment yang belum punya submission
            val missingAssignments = allAssignments.filter {
                !existingAssignmentIds.contains(it.assignmentId)
            }

            // 5. Insert submission dummy untuk assignment yang belum punya
            for (assignment in missingAssignments) {
                val newSubmission = SubmissionData(
                    submissionId = UUID.randomUUID().toString(),
                    assignmentId = assignment.assignmentId,
                    userId = userId,
                    isSubmitted = false,
                    createdAt = Clock.System.now().toString()
                )

                supabase.postgrest["submissions"].insert(newSubmission)
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
            val data = EnrollmentData(
                userId = userId,
                courseId = courseId
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
                val data = SubmissionData(
                    submissionId = UUID.randomUUID().toString(),
                    userId = userId.toString(),
                    assignmentId = assignment.assignmentId,
                    isSubmitted = false,
                    createdAt = Clock.System.now().toString()
                )
                supabase.postgrest["submissions"].insert(data)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun submitAssignmentWithFile(
        userId: String,
        assignmentId: String,
        fileInputStream: InputStream,
        fileName: String
    ): Flow<Result<Unit>> = flow {
        try {

            // 1. Generate unique file path
            val timestamp = Clock.System.now().epochSeconds
            val fileExtension = fileName.substringAfterLast(".", "")
            val uniqueFileName = "${userId}_${assignmentId}_${timestamp}.${fileExtension}"
            val filePath = "submissions/$uniqueFileName"

            Log.d(TAG, "Uploading file to path: $filePath")

            // 2. Upload file to Supabase Storage
            val fileBytes = fileInputStream.readBytes()
            supabase.storage[ASSIGNMENT_FILES_BUCKET].upload(
                path = filePath,
                data = fileBytes
            ) {
                upsert = true
            }

            Log.d(TAG, "File uploaded successfully")

            // 3. Get file URL
            val fileUrl = supabase.storage[ASSIGNMENT_FILES_BUCKET].publicUrl(filePath)
            Log.d(TAG, "File URL: $fileUrl")

            // 4. Update submission record
            val updateData = SubmissionUpdateData(
                fileUrl = fileUrl,
                fileName = fileName,
                isSubmitted = true,
                submittedAt = Clock.System.now().toString()
            )

            // First, try to find existing submission
            val existingSubmissions = supabase.postgrest[SUBMISSIONS_TABLE]
                .select {
                    filter {
                        eq("user_id", userId)
                        eq("assignment_id", assignmentId)
                    }
                }
                .decodeList<Submission>()

            if (existingSubmissions.any { it.isSubmitted }) {
                emit(Result.failure(Exception("You have already submitted this assignment.")))
                return@flow
            }

            if (existingSubmissions.isNotEmpty()) {
                // Update existing submission
                Log.d(TAG, "Updating existing submission")
                supabase.postgrest[SUBMISSIONS_TABLE]
                    .update(updateData) {
                        filter {
                            eq("user_id", userId)
                            eq("assignment_id", assignmentId)
                        }
                    }
            } else {
                Log.d(TAG, "Creating new submission")
                val newSubmissionData = SubmissionData(
                    submissionId = UUID.randomUUID().toString(),
                    userId = userId,
                    assignmentId = assignmentId,
                    fileUrl = fileUrl,
                    fileName = fileName,
                    isSubmitted = true,
                    createdAt = Clock.System.now().toString(),
                    submittedAt = Clock.System.now().toString()
                )
                supabase.postgrest[SUBMISSIONS_TABLE].insert(newSubmissionData)
            }

            Log.d(TAG, "Submission completed successfully")
            emit(Result.success(Unit))

        } catch (e: Exception) {
            Log.e(TAG, "Error in submitAssignmentWithFile", e)
            emit(Result.failure(e))
        } finally {
            try {
                fileInputStream.close()
            } catch (e: Exception) {
                Log.w(TAG, "Error closing input stream", e)
            }
        }
    }
}