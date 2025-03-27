package com.example.therapy_flow.network.dtos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ConsultationDTO(
    @Json(name = "id")
    val id: String,
    @Json(name = "date_time")
    val dateTime: String,
    @Json(name = "status")
    val status: String,
    @Json(name = "description")
    val description: String?,
    @Json(name = "creator_id")
    val creatorId: String,
    @Json(name = "patient_id")
    val patientId: String,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "updated_at")
    val updatedAt: String
)

@JsonClass(generateAdapter = true)
data class CreateConsultationDTO(
    @Json(name = "date_time")
    val dateTime: String,
    @Json(name = "status")
    val status: String,
    @Json(name = "description")
    val description: String?,
    @Json(name = "creator_id")
    val creatorId: String,
    @Json(name = "patient_id")
    val patientId: String,
)

@JsonClass(generateAdapter = true)
data class UpdateConsultationDTO(
    @Json(name = "date_time")
    val dateTime: String?,
    @Json(name = "status")
    val status: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "creator_id")
    val creatorId: String?,
    @Json(name = "patient_id")
    val patientId: String?,
)

