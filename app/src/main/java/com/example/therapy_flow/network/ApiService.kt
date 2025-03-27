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
import retrofit2.http.PATCH

interface ApiService {

    @POST(ApiRoutes.LOGIN)
    suspend fun login(
        @Query("grant_type") grantType: String = "password",
        @Body loginRequest: LoginRequestDTO
    ): Response<AuthResponseDTO>?

    @POST(ApiRoutes.REGISTER)
    suspend fun register(
        @Body registerRequest: RegisterRequestDTO
    ): Response<TherapistDTO>?

    @GET(ApiRoutes.PATIENT)
    suspend fun getPatients(@Query("therapist_id") filter: String): Response<List<PatientDTO>>?

    @POST(ApiRoutes.PATIENT)
    suspend fun createPatient(
        @Body patient: CreatePatientDTO
    ): Response<List<PatientDTO>>?

    @PATCH(ApiRoutes.PATIENT)
    suspend fun updatePatient(
        @Query("id") id: String,
        @Body patient: UpdatePatientDTO
    ): Response<List<PatientDTO>>?

    @DELETE(ApiRoutes.PATIENT)
    suspend fun deletePatient(@Query("id") id: String): Response<List<PatientDTO>>?

    @GET(ApiRoutes.CONSULTATION)
    suspend fun getConsultations(@Query("creator_id") filter: String): Response<List<ConsultationDTO>>?

    @GET(ApiRoutes.CONSULTATION)
    suspend fun getConsultation(@Query("id") uid: String): Response<List<ConsultationDTO>>?

    @PUT(ApiRoutes.CONSULTATION)
    suspend fun updateConsultation(
        @Query("id") uid: String,
        @Body consultation: UpdateConsultationDTO
    ): Response<List<ConsultationDTO>>?

    @DELETE(ApiRoutes.CONSULTATION)
    suspend fun deleteConsultation(@Query("id") uid: String): Response<List<ConsultationDTO>>?

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

    @GET(ApiRoutes.THERAPIST)
    suspend fun getTherapist(@Query("id") uid: String): Response<List<TherapistDTO>>?

    @PATCH(ApiRoutes.THERAPIST)
    suspend fun updateTherapist(
        @Query("id") uid: String,
        @Body therapist: UpdateTherapistDTO
    ): Response<List<TherapistDTO>>?
}
