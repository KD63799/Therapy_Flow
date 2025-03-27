package com.example.therapy_flow.userInterface.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.therapy_flow.R
import com.example.therapy_flow.network.ApiService
import com.example.therapy_flow.utils.PreferencesManager
import com.example.therapy_flow.utils.Therapist
import com.example.therapy_flow.utils.toDomainModel
import com.example.therapy_flow.utils.toUpdateDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferences: PreferencesManager,
    private val apiService: ApiService
) : ViewModel() {

    private val _uiMessageFlow = MutableSharedFlow<String>()
    val uiMessageFlow = _uiMessageFlow.asSharedFlow()

    private val _therapistDataFlow = MutableStateFlow<Therapist?>(null)
    val therapistDataFlow: StateFlow<Therapist?> = _therapistDataFlow

    init {
        val therapistId = preferences.currentUserId ?: ""
        if (therapistId.isNotEmpty()) {
            viewModelScope.launch {
                try {
                    val response = apiService.getTherapist("eq.$therapistId")
                    if (response?.isSuccessful == true) {
                        response.body()?.firstOrNull()?.let { therapistDTO ->
                            _therapistDataFlow.value = therapistDTO.toDomainModel()
                        }
                    } else {
                        _uiMessageFlow.emit("Erreur lors de la récupération du profil: code ${response?.code()}")
                    }
                } catch (e: Exception) {
                    _uiMessageFlow.emit("Erreur inattendue: ${e.message}")
                }
            }
        }
    }

    fun updateTherapist(updatedTherapist: Therapist) {
        viewModelScope.launch {
            try {
                val updateDTO = updatedTherapist.toUpdateDTO()
                val response = apiService.updateTherapist("eq.${updatedTherapist.id}", updateDTO)
                if (response?.isSuccessful == true) {
                    _therapistDataFlow.value = updatedTherapist
                    _uiMessageFlow.emit(context.getString(R.string.profile_updated))
                } else {
                    _uiMessageFlow.emit("Error while updated data: code ${response?.code()}")
                }
            } catch (e: Exception) {
                _uiMessageFlow.emit("Unexpected error\n ${e.message}")
            }
        }
    }
}
