package com.chrisp.setaraapp.feature.sekerja.detailTugas.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Submission(
    @SerialName("submission_id") val submissionId: String,
    @SerialName("assignment_id") val assignmentId: String,
    @SerialName("user_id") val userId: String,
    @SerialName("file_path") val fileUrl: String? = null, // Changed from file_path to file_url
    @SerialName("file_name") val fileName: String? = null,
    @SerialName("is_submitted") val isSubmitted: Boolean,
    @SerialName("submitted_at") val submittedAt: String? = null,
    @SerialName("created_at") val createdAt: String
)