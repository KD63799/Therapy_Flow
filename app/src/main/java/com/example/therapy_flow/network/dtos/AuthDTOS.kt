package com.example.therapy_flow.network.dtos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequestDTO(
    @Json(name = "email")
    val email: String,
    @Json(name = "password")
    val password: String
)

@JsonClass(generateAdapter = true)
data class RegisterRequestDTO(
    @Json(name = "firstName")
    val firstName: String,
    @Json(name = "lastName")
    val lastName: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "password")
    val password: String
)

data class AuthTokenDTO(
    @Json(name = "access_token")
    val accessToken: String
)
