package com.example.therapy_flow.utils

import com.example.therapy_flow.network.dtos.TherapistDTO
import com.example.therapy_flow.network.dtos.UpdateTherapistDTO

// Conversion du DTO en modèle de domaine
fun TherapistDTO.toDomainModel(): Therapist {
    return Therapist(
        id = this.id,
        name = "$firstName $lastName", // Concaténation du prénom et du nom
        email = this.email
    )
}

// Conversion du modèle de domaine en DTO pour la mise à jour
fun Therapist.toUpdateDTO(): UpdateTherapistDTO {
    // Supposons que 'name' soit sous la forme "Prénom Nom"
    val nameParts = this.name.split(" ", limit = 2)
    val firstName = nameParts.getOrElse(0) { "" }
    val lastName = if (nameParts.size > 1) nameParts[1] else ""
    return UpdateTherapistDTO(
        firstName = firstName,
        lastName = lastName,
        email = this.email
    )
}


// Modèle de domaine pour le thérapeute
data class Therapist(
    val id: String,
    val name: String,
    val email: String
)
