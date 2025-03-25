package com.example.therapy_flow.userInterface.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.therapy_flow.network.ApiService
import com.example.therapy_flow.network.dtos.TherapistDTO
import com.example.therapy_flow.network.dtos.UpdateTherapistDTO
import com.example.therapy_flow.utils.PreferencesManager
import com.example.therapy_flow.utils.Therapist
import com.example.therapy_flow.utils.toDomainModel
import com.example.therapy_flow.utils.toUpdateDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val preferences: PreferencesManager,
    private val apiService: ApiService
) : ViewModel() {

    private val _therapistData = MutableStateFlow<Therapist?>(null)
    val therapistData: StateFlow<Therapist?> = _therapistData

    init {
        // Récupération de l'ID du thérapeute depuis les préférences
        val therapistId = preferences.currentUserId ?: ""
        if (therapistId.isNotEmpty()) {
            viewModelScope.launch {
                try {
                    val response = apiService.getTherapist(therapistId)
                    if (response?.isSuccessful == true) {
                        response.body()?.firstOrNull()?.let { therapistDTO ->
                            _therapistData.value = therapistDTO.toDomainModel()
                        }
                    } else {
                        // Gestion de l'erreur côté API
                    }
                } catch (e: Exception) {
                    // Gestion de l'exception (log, message utilisateur, etc.)
                }
            }
        }
    }

    fun updateTherapist(updatedTherapist: Therapist) {
        viewModelScope.launch {
            try {
                // Conversion du modèle de domaine en DTO pour la mise à jour
                val updateDTO = updatedTherapist.toUpdateDTO()
                val response = apiService.updateTherapist(updatedTherapist.id, updateDTO)
                if (response?.isSuccessful == true) {
                    // Mise à jour du state avec la réponse de l'API
                    response.body()?.firstOrNull()?.let { updatedDTO ->
                        _therapistData.value = updatedDTO.toDomainModel()
                    }
                } else {
                    // Gestion d'une réponse non satisfaisante
                }
            } catch (e: Exception) {
                // Gestion de l'exception
            }
        }
    }
}
