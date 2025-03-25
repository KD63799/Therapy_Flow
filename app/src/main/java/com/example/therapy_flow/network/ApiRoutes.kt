package com.example.therapy_flow.network

object ApiRoutes {

    const val BASE_URL = "https://puanogfgpdnpkvhhwsxv.supabase.co/rest/v1/"
    const val BASE_URL_AUTH = "https://puanogfgpdnpkvhhwsxv.supabase.co/auth/v1/"
    const val API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InB1YW5vZ2ZncGRucGt2aGh3c3h2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDExODMyMDAsImV4cCI6MjA1Njc1OTIwMH0.p2fQHMbggWkHUSg5bXv5n1uG25RbHTwDBqqkjrZwqRw"

    // Endpoints d'authentification pour les thérapeutes
    const val LOGIN = "$BASE_URL_AUTH/token"
    const val REGISTER = "$BASE_URL_AUTH/signup"

    // Endpoints pour les thérapeutes
    const val THERAPIST = "$BASE_URL/therapist"
    const val THERAPIST_DETAIL = "$BASE_URL/therapist/eq.{uid}"

    // Endpoints pour les patients
    const val PATIENTS = "$BASE_URL/patient"
    const val PATIENTS_BY_THERAPIST = "$BASE_URL/patient?therapist_id=eq."
    const val PATIENT_DETAIL = "$BASE_URL/patient/eq.{uid}"

    // Endpoints pour les consultations
    const val CONSULTATIONS = "$BASE_URL/consultations"
    const val CONSULTATION_DETAIL = "$BASE_URL/consultations/eq.{uid}"

    // Endpoints pour la to-do list (exclusivement pour les thérapeutes)
    const val TODOS = "$BASE_URL/todo"
    const val TODO_DETAIL = "$BASE_URL/todo/{uid}"

    // Endpoints pour les catégories (catégorisation des consultations)
    const val CATEGORIES = "$BASE_URL/categories"
    const val CATEGORY_DETAIL = "$BASE_URL/categories/eq.{uid}"

    // Endpoint pour associer un patient à un thérapeute (relation HEAL)
    const val THERAPIST_PATIENT_ASSOCIATE = "$BASE_URL/therapists/eq.{therapistUid}/patients/eq.{patientUid}"
}
