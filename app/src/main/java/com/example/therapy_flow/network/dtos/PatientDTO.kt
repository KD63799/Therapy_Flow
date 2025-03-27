package com.example.therapy_flow.network.dtos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PatientDTO(
    @Json(name = "id")
    val id: String,
    @Json(name = "first_name")
    val firstName: String,
    @Json(name = "last_name")
    val lastName: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "birthdate")
    val birthdate: String,
    @Json(name = "phone")
    val phone: String
)

@JsonClass(generateAdapter = true)
data class CreatePatientDTO(
    @Json(name = "first_name")
    val firstName: String,
    @Json(name = "last_name")
    val lastName: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "birthdate")
    val birthdate: String,
    @Json(name = "phone")
    val phone: String,
    @Json(name = "therapist_id")
    val therapistId: String
)


@JsonClass(generateAdapter = true)
data class UpdatePatientDTO(
    @Json(name = "first_name")
    val firstName: String?,
    @Json(name = "last_name")
    val lastName: String?,
    @Json(name = "email")
    val email: String?,
    @Json(name = "birthdate")
    val birthdate: String?,
    @Json(name = "phone")
    val phone: String?
)

