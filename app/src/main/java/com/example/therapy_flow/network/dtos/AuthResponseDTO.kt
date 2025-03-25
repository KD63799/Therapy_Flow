package com.example.therapy_flow.network.dtos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthResponseDTO(
    @Json(name = "access_token")
    val token: String? = null,

    val code: Int? = null,
    @Json(name = "error_code")
    val errorCode: String? = null,
    val msg: String? = null,

    val user: User? = null
) {
    @JsonClass(generateAdapter = true)
    data class User(
        val id: String? = null
    )
}
