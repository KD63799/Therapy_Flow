package com.example.therapy_flow.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import com.example.therapy_flow.network.dtos.*

interface ApiService {

    // Authentification
    @POST(ApiRoutes.LOGIN)
    suspend fun login(
        @Query("grant_type") grantType: String = "password",
        @Body loginRequest: LoginRequestDTO
    ): Response<AuthResponseDTO>?

    @POST(ApiRoutes.REGISTER)
    suspend fun register(
        @Body registerRequest: RegisterRequestDTO
    ): Response<TherapistDTO>?

    // Patients
    @GET(ApiRoutes.PATIENTS)
    suspend fun getPatients(@Query("therapist_id") therapistId: String): Response<List<PatientDTO>>?

    @GET(ApiRoutes.PATIENT_DETAIL)
    suspend fun getPatient(@Path("uid") uid: String): Response<List<PatientDTO>>?

    @POST(ApiRoutes.PATIENTS)
    suspend fun createPatient(
        @Body patient: CreatePatientDTO
    ): Response<List<PatientDTO>>?

    @PUT(ApiRoutes.PATIENT_DETAIL)
    suspend fun updatePatient(
        @Path("uid") uid: String,
        @Body patient: UpdatePatientDTO
    ): Response<List<PatientDTO>>?

    @DELETE(ApiRoutes.PATIENT_DETAIL)
    suspend fun deletePatient(@Path("uid") uid: String): Response<List<PatientDTO>>?

    // Consultations
    @GET(ApiRoutes.CONSULTATIONS)
    suspend fun getConsultations(): Response<List<ConsultationDTO>>?

    @GET(ApiRoutes.CONSULTATION_DETAIL)
    suspend fun getConsultation(@Path("uid") uid: String): Response<List<ConsultationDTO>>?

    @POST(ApiRoutes.CONSULTATIONS)
    suspend fun createConsultation(
        @Body consultation: CreateConsultationDTO
    ): Response<List<ConsultationDTO>>?

    @PUT(ApiRoutes.CONSULTATION_DETAIL)
    suspend fun updateConsultation(
        @Path("uid") uid: String,
        @Body consultation: UpdateConsultationDTO
    ): Response<List<ConsultationDTO>>?

    @DELETE(ApiRoutes.CONSULTATION_DETAIL)
    suspend fun deleteConsultation(@Path("uid") uid: String): Response<List<ConsultationDTO>>?

    // Todo (to-do list pour le thérapeute)
    @GET(ApiRoutes.TODOS)
    suspend fun getTodos(): Response<List<TodoDTO>>?

    @GET(ApiRoutes.TODO_DETAIL)
    suspend fun getTodo(@Path("uid") uid: String): Response<List<TodoDTO>>?

    @POST(ApiRoutes.TODOS)
    suspend fun createTodo(
        @Body todo: CreateTodoDTO
    ): Response<List<TodoDTO>>?

    @PUT(ApiRoutes.TODO_DETAIL)
    suspend fun updateTodo(
        @Path("uid") uid: String,
        @Body todo: UpdateTodoDTO
    ): Response<List<TodoDTO>>?

    @DELETE(ApiRoutes.TODO_DETAIL)
    suspend fun deleteTodo(@Path("uid") uid: String): Response<List<TodoDTO>>?

    // Therapists
    @GET(ApiRoutes.THERAPIST)
    suspend fun getTherapists(): Response<List<TherapistDTO>>?

    @GET(ApiRoutes.THERAPIST_DETAIL)
    suspend fun getTherapist(@Path("uid") uid: String): Response<List<TherapistDTO>>?

    @PUT(ApiRoutes.THERAPIST_DETAIL)
    suspend fun updateTherapist(
        @Path("uid") uid: String,
        @Body therapist: UpdateTherapistDTO
    ): Response<List<TherapistDTO>>?

    // Categories
    @GET(ApiRoutes.CATEGORIES)
    suspend fun getCategories(): Response<List<CategoryDTO>>?

    @GET(ApiRoutes.CATEGORY_DETAIL)
    suspend fun getCategory(@Path("uid") uid: String): Response<List<CategoryDTO>>?

    @POST(ApiRoutes.CATEGORIES)
    suspend fun createCategory(
        @Body category: CreateCategoryDTO
    ): Response<List<CategoryDTO>>?

    @PUT(ApiRoutes.CATEGORY_DETAIL)
    suspend fun updateCategory(
        @Path("uid") uid: String,
        @Body category: UpdateCategoryDTO
    ): Response<List<CategoryDTO>>?

    @DELETE(ApiRoutes.CATEGORY_DETAIL)
    suspend fun deleteCategory(@Path("uid") uid: String): Response<List<CategoryDTO>>?
}

