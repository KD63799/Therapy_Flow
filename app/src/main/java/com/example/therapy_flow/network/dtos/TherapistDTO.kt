package com.example.therapy_flow.network.dtos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TherapistDTO(
    @Json(name = "id")
    val id: String,  // UUID sous forme de String
    @Json(name = "first_name")
    val firstName: String,
    @Json(name = "last_name")
    val lastName: String,
    @Json(name = "email")
    val email: String
)

@JsonClass(generateAdapter = true)
data class UpdateTherapistDTO(
    @Json(name = "first_name")
    val firstName: String?,
    @Json(name = "last_name")
    val lastName: String?,
    @Json(name = "email")
    val email: String?
)

