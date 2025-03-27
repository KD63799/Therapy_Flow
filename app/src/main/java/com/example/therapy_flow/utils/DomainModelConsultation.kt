package com.example.therapy_flow.utils

import com.example.therapy_flow.network.dtos.ConsultationDTO

data class Consultation(
    val id: String,
    val patient: String,
    val scheduledAt: String,
    val description: String,
    val status: String
)

fun ConsultationDTO.toDomainModel(): Consultation {
    return Consultation(
        id = this.id,
        patient = this.patientId,
        scheduledAt = this.dateTime,
        description = this.description ?: "",
        status = this.status
    )
}