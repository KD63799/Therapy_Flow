package com.example.therapy_flow.utils

import com.example.therapy_flow.network.dtos.PatientDTO
import com.example.therapy_flow.network.dtos.UpdatePatientDTO

data class Patient(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val birthdate: String,
    val phone: String
)

fun PatientDTO.toDomainModel(): Patient {
    return Patient(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email,
        birthdate = this.birthdate,
        phone = this.phone
    )
}

fun Patient.toUpdateDTO(): UpdatePatientDTO {
    return UpdatePatientDTO(
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email,
        birthdate = this.birthdate,
        phone = this.phone
    )
}
