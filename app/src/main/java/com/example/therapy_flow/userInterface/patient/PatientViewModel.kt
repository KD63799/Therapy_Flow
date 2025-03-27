package com.example.therapy_flow.userInterface.patient

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.therapy_flow.R
import com.example.therapy_flow.network.ApiService
import com.example.therapy_flow.network.dtos.CreatePatientDTO
import com.example.therapy_flow.utils.Patient
import com.example.therapy_flow.utils.PreferencesManager
import com.example.therapy_flow.utils.toDomainModel
import com.example.therapy_flow.utils.toUpdateDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferences: PreferencesManager,
    private val apiService: ApiService
) : ViewModel() {

    private val _listPatientsFlow = MutableStateFlow<List<Patient>>(emptyList())
    val listPatientsFlow: StateFlow<List<Patient>> = _listPatientsFlow

    private val _isFormExpanded = MutableStateFlow(false)
    val isFormExpanded: StateFlow<Boolean> = _isFormExpanded

    private val _uiMessageFlow = MutableSharedFlow<String>()
    val uiMessageFlow = _uiMessageFlow.asSharedFlow()

    init {
        fetchPatients()
    }

    fun toggleForm() {
        _isFormExpanded.value = !_isFormExpanded.value
    }

    fun fetchPatients() {
        val therapistId = preferences.currentUserId ?: ""
        if (therapistId.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val response = apiService.getPatients("eq.$therapistId")
                    if (response?.isSuccessful == true) {
                        val patientDTOs = response.body() ?: emptyList()
                        _listPatientsFlow.value = patientDTOs.map { it.toDomainModel() }
                    } else {
                        _uiMessageFlow.emit("Erreur de récupération: ${response?.code()}")
                    }
                } catch (e: Exception) {
                    _uiMessageFlow.emit("Erreur: ${e.message}")
                }
            }
        }
    }

    fun addPatient(firstName: String, lastName: String, email: String, birthdate: String, phone: String) {
        val therapistId = preferences.currentUserId ?: ""
        if (therapistId.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val createDTO = CreatePatientDTO(
                        firstName = firstName,
                        lastName = lastName,
                        email = email,
                        birthdate = birthdate,
                        phone = phone,
                        therapistId = therapistId
                    )
                    val response = apiService.createPatient(createDTO)
                    if (response?.isSuccessful == true) {
                        fetchPatients()
                        _uiMessageFlow.emit(context.getString(R.string.patient_added))
                    } else {
                        _uiMessageFlow.emit("Error : ${response?.code()}")
                    }
                } catch (e: Exception) {
                    _uiMessageFlow.emit("Error while addPatient: ${e.message}")
                }
            }
        }
    }

    fun updatePatient(patient: Patient) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val updateDTO = patient.toUpdateDTO()
                val response = apiService.updatePatient("eq.${patient.id}", updateDTO)
                if (response?.isSuccessful == true) {
                    fetchPatients()
                    _uiMessageFlow.emit(context.getString(R.string.patient_updated))
                } else {
                    _uiMessageFlow.emit("Erreur de mise à jour: ${response?.code()}")
                }
            } catch (e: Exception) {
                _uiMessageFlow.emit("Erreur while update Patient: ${e.message}")
            }
        }
    }

    fun deletePatient(patientId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.deletePatient("eq.$patientId")
                if (response?.isSuccessful == true) {
                    fetchPatients()
                    _uiMessageFlow.emit(context.getString(R.string.patient_deleted))
                } else {
                    _uiMessageFlow.emit("Error code: ${response?.code()}")
                }
            } catch (e: Exception) {
                _uiMessageFlow.emit("Error while delete: ${e.message}")
            }
        }
    }
}
