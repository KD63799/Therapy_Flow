package com.example.therapy_flow.userInterface.patient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.therapy_flow.design_system.Patient
import com.example.therapy_flow.network.ApiService
import com.example.therapy_flow.network.dtos.CreatePatientDTO
import com.example.therapy_flow.network.dtos.PatientDTO
import com.example.therapy_flow.network.dtos.UpdatePatientDTO
import com.example.therapy_flow.utils.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PatientViewModel @Inject constructor(
    private val preferences: PreferencesManager,
    private val apiService: ApiService
) : ViewModel() {

    // État de la liste des patients
    private val _patients = MutableStateFlow<List<Patient>>(emptyList())
    val patients = _patients.asStateFlow()

    // SharedFlow pour envoyer des messages toast à l'UI
    private val _toastMessageFlow = MutableSharedFlow<String>()
    val toastMessageFlow = _toastMessageFlow.asSharedFlow()

    // État pour l'expansion du formulaire (géré dans le ViewModel)
    private val _isFormExpanded = MutableStateFlow(false)
    val isFormExpanded = _isFormExpanded.asStateFlow()

    init {
        loadPatients()
    }

    fun setFormExpanded(expanded: Boolean) {
        _isFormExpanded.value = expanded
    }

    fun toggleForm() {
        _isFormExpanded.value = !_isFormExpanded.value
    }

    // Chargement des patients du thérapeute via l'API
    fun loadPatients() {
        viewModelScope.launch {
            try {
                val therapistId = preferences.currentUserId
                if (therapistId.isNullOrBlank()) {
                    _toastMessageFlow.emit("Therapist ID is missing")
                    return@launch
                }
                // On ajoute "eq." devant l'ID pour respecter le format attendu
                val response: Response<List<PatientDTO>>? = withContext(Dispatchers.IO) {
                    apiService.getPatients("eq.$therapistId")
                }
                if (response != null && response.isSuccessful) {
                    val patientsDto = response.body() ?: emptyList()
                    val patientList = patientsDto.map { dto ->
                        Patient(
                            id = dto.id,
                            firstName = dto.firstName,
                            lastName = dto.lastName,
                            email = dto.email,
                            birthdate = dto.birthdate,
                            phone = dto.phone
                        )
                    }
                    _patients.value = patientList
                } else {
                    _toastMessageFlow.emit("Error loading patients")
                }
            } catch (e: Exception) {
                _toastMessageFlow.emit("Exception: ${e.message}")
            }
        }
    }

    // Ajout d'un patient via l'API
    fun addPatient(firstName: String, lastName: String, email: String, birthdate: String, phone: String) {
        viewModelScope.launch {
            try {
                val therapistId = preferences.currentUserId
                if (therapistId.isNullOrBlank()) {
                    _toastMessageFlow.emit("Therapist ID is missing")
                    return@launch
                }
                val createDto = CreatePatientDTO(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    birthdate = birthdate,
                    phone = phone,
                    therapistId = therapistId
                )
                val response: Response<List<PatientDTO>>? = withContext(Dispatchers.IO) {
                    apiService.createPatient(createDto)
                }
                if (response != null && response.isSuccessful) {
                    _toastMessageFlow.emit("Patient added successfully")
                    loadPatients() // Recharge la liste
                    setFormExpanded(false) // Ferme le formulaire
                } else {
                    _toastMessageFlow.emit("Failed to add patient")
                }
            } catch (e: Exception) {
                _toastMessageFlow.emit("Error adding patient: ${e.message}")
            }
        }
    }

    // Fonction pour mettre à jour un patient via l'API
    fun updatePatient(patient: Patient) {
        viewModelScope.launch {
            try {
                val updateDto = UpdatePatientDTO(
                    firstName = patient.firstName,
                    lastName = patient.lastName,
                    email = patient.email,
                    birthdate = patient.birthdate,
                    phone = patient.phone
                )
                val response: Response<List<PatientDTO>>? = withContext(Dispatchers.IO) {
                    apiService.updatePatient(patient.id, updateDto)
                }
                if (response != null && response.isSuccessful) {
                    _toastMessageFlow.emit("Patient updated successfully")
                    loadPatients()
                } else {
                    _toastMessageFlow.emit("Failed to update patient")
                }
            } catch (e: Exception) {
                _toastMessageFlow.emit("Error updating patient: ${e.message}")
            }
        }
    }

    // Fonction pour supprimer un patient via l'API
    fun deletePatient(patientId: String) {
        viewModelScope.launch {
            try {
                val response: Response<List<PatientDTO>>? = withContext(Dispatchers.IO) {
                    apiService.deletePatient(patientId)
                }
                if (response != null && response.isSuccessful) {
                    _toastMessageFlow.emit("Patient deleted successfully")
                    loadPatients()
                } else {
                    _toastMessageFlow.emit("Failed to delete patient")
                }
            } catch (e: Exception) {
                _toastMessageFlow.emit("Error deleting patient: ${e.message}")
            }
        }
    }
}

