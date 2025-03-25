package com.example.therapy_flow.userInterface.register

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.therapy_flow.network.ApiService
import com.example.therapy_flow.network.dtos.AuthResponseDTO
import com.example.therapy_flow.network.dtos.LoginRequestDTO
import com.example.therapy_flow.network.dtos.RegisterRequestDTO
import com.example.therapy_flow.utils.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.ConnectException
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val apiService: ApiService,
    private val preferences: PreferencesManager
) : ViewModel() {

    // SharedFlow pour notifier la navigation
    private val _registerResult = MutableSharedFlow<Boolean>()
    val registerResult = _registerResult.asSharedFlow()

    // SharedFlow pour envoyer des messages toast à l'UI
    private val _toastMessageFlow = MutableSharedFlow<String>()
    val toastMessageFlow = _toastMessageFlow.asSharedFlow()

    // Regex pour la validation d'email dans le ViewModel
    private val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})".toRegex()

    fun performRegister(email: String, password: String, confirmPassword: String) {
        // Vérification des champs
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            viewModelScope.launch {
                _toastMessageFlow.emit("Please fill in all fields")
                _registerResult.emit(false)
            }
            return
        }
        if (password != confirmPassword) {
            viewModelScope.launch {
                _toastMessageFlow.emit("Passwords do not match")
                _registerResult.emit(false)
            }
            return
        }
        // Vérification du format d'email
        if (!emailRegex.matches(email)) {
            viewModelScope.launch {
                _toastMessageFlow.emit("Invalid email format")
                _registerResult.emit(false)
            }
            return
        }

        viewModelScope.launch {
            try {
                // Appel à l'endpoint register pour créer l'utilisateur.
                // Le trigger se chargera d'insérer dans therapist.
                val registerRequest = RegisterRequestDTO(
                    firstName = "Enter your first name",
                    lastName = "Enter your last name",
                    email = email,
                    password = password
                )
                val registerResponse = withContext(Dispatchers.IO) {
                    apiService.register(registerRequest)
                }
                if (registerResponse?.code() != 200) {
                    _toastMessageFlow.emit("Registration failed")
                    _registerResult.emit(false)
                    return@launch
                }

                // Appel à l'endpoint login pour obtenir le token et l'ID utilisateur (therapist).
                val loginRequest = LoginRequestDTO(email = email, password = password)
                val loginResponse = withContext(Dispatchers.IO) {
                    apiService.login(grantType = "password", loginRequest = loginRequest)
                }
                val authResponse: AuthResponseDTO? = loginResponse?.body()
                if (loginResponse?.code() == 200 && authResponse != null) {
                    // Stockage du token et de l'ID dans les Preferences.
                    preferences.authToken = authResponse.token
                    preferences.currentUserId = authResponse.user?.id
                    _toastMessageFlow.emit("Registration successful")
                    _registerResult.emit(true)
                } else {
                    _toastMessageFlow.emit("Login failed after registration")
                    _registerResult.emit(false)
                }
            } catch (ex: ConnectException) {
                _toastMessageFlow.emit("Connection error")
                _registerResult.emit(false)
            } catch (ex: Exception) {
                _toastMessageFlow.emit("Unexpected error: ${ex.message}")
                _registerResult.emit(false)
            }
        }
    }
}
