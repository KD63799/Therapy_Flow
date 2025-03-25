package com.example.therapy_flow.network.dtos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoryDTO(
    @Json(name = "id")
    val id: String,
    @Json(name = "category_name")
    val categoryName: String
)

@JsonClass(generateAdapter = true)
data class CreateCategoryDTO(
    @Json(name = "category_name")
    val categoryName: String
)

@JsonClass(generateAdapter = true)
data class UpdateCategoryDTO(
    @Json(name = "category_name")
    val categoryName: String?
)
