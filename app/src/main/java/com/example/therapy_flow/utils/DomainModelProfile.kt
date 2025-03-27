package com.example.therapy_flow.utils

import com.example.therapy_flow.network.dtos.TherapistDTO
import com.example.therapy_flow.network.dtos.UpdateTherapistDTO

data class Therapist(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String
)

fun TherapistDTO.toDomainModel(): Therapist {
    return Therapist(
        id = this.id,
        firstName = this.firstName ?: "",
        lastName = this.lastName ?: "",
        email = this.email
    )
}

fun Therapist.toUpdateDTO(): UpdateTherapistDTO {
    return UpdateTherapistDTO(
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email
    )
}
