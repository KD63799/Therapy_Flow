package com.example.therapy_flow.userInterface.schedule

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.therapy_flow.network.ApiService
import com.example.therapy_flow.utils.Consultation
import com.example.therapy_flow.utils.PreferencesManager
import com.example.therapy_flow.utils.toDomainModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val preferences: PreferencesManager,
    private val apiService: ApiService
) : ViewModel() {

    private val _consultationsStateFlow = MutableStateFlow<List<Consultation>>(emptyList())
    val consultationsStateFlow = _consultationsStateFlow.asStateFlow()

    init {
        loadConsultations()
    }

    private fun loadConsultations() {
        val therapistId = preferences.currentUserId ?: ""
        if (therapistId.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val consultationsResponse = apiService.getConsultations("eq.$therapistId")
                    if (consultationsResponse?.isSuccessful == true) {
                        val consultationDTOs = consultationsResponse.body() ?: emptyList()
                        var consultations = consultationDTOs.map { it.toDomainModel() }

                        val patientsResponse = apiService.getPatients("eq.$therapistId")
                        if (patientsResponse?.isSuccessful == true) {
                            val patientDTOs = patientsResponse.body() ?: emptyList()
                            val patientMap = patientDTOs.associateBy(
                                keySelector = { it.id },
                                valueTransform = { "${it.firstName} ${it.lastName}" }
                            )
                            consultations = consultations.map { consultation ->
                                val patientName = patientMap[consultation.patient] ?: consultation.patient
                                consultation.copy(patient = patientName)
                            }
                        }
                        Log.d("ScheduleViewModel", "Consultations récupérées: ${consultations.size}")
                        launch(Dispatchers.Main) {
                            _consultationsStateFlow.value = consultations
                        }
                    } else {
                        Log.e("ScheduleViewModel", "Erreur API: ${consultationsResponse?.code()}")
                    }
                } catch (e: Exception) {
                    Log.e("ScheduleViewModel", "Exception dans loadConsultations: ${e.message}")
                }
            }
        } else {
            Log.e("ScheduleViewModel", "therapistId vide")
        }
    }
}
