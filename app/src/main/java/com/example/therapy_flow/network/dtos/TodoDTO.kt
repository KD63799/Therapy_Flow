package com.example.therapy_flow.network.dtos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TodoDTO(
    @Json(name = "id")
    val id: String,
    @Json(name = "task_name")
    val taskName: String,
    @Json(name = "description")
    val description: String?,
    @Json(name = "status")
    val status: String,
    @Json(name = "therapist_id")
    val therapistId: String,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "updated_at")
    val updatedAt: String
)

@JsonClass(generateAdapter = true)
data class CreateTodoDTO(
    @Json(name = "task_name")
    val taskName: String,
    @Json(name = "description")
    val description: String?,
    @Json(name = "status")
    val status: String,
    @Json(name = "therapist_id")
    val therapistId: String
)

@JsonClass(generateAdapter = true)
data class UpdateTodoDTO(
    @Json(name = "task_name")
    val taskName: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "status")
    val status: String?
)

