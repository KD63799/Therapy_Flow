package com.example.therapy_flow.userInterface.register

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.therapy_flow.R
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
    @ApplicationContext private val context: Context,
    private val apiService: ApiService,
    private val preferences: PreferencesManager
) : ViewModel() {

    private val _registerResult = MutableSharedFlow<Boolean>()
    val registerResult = _registerResult.asSharedFlow()

    private val _uiMessageFlow = MutableSharedFlow<String>()
    val uiMessageFlow = _uiMessageFlow.asSharedFlow()

    private val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})".toRegex()

    fun performRegister(email: String, password: String, confirmPassword: String) {
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            viewModelScope.launch {
                _uiMessageFlow.emit(context.getString(R.string.please_fill_in_all_fields))
                _registerResult.emit(false)
            }
            return
        }
        if (password != confirmPassword) {
            viewModelScope.launch {
                _uiMessageFlow.emit(context.getString(R.string.password_not_match))
                _registerResult.emit(false)
            }
            return
        }
        if (!emailRegex.matches(email)) {
            viewModelScope.launch {
                _uiMessageFlow.emit(context.getString(R.string.invalid_email_format))
                _registerResult.emit(false)
            }
            return
        }

        viewModelScope.launch {
            try {
                val registerRequest = RegisterRequestDTO(
                    firstName = context.getString(R.string.enter_your_first_name),
                    lastName = context.getString(R.string.enter_your_last_name),
                    email = email,
                    password = password
                )
                val registerResponse = withContext(Dispatchers.IO) {
                    apiService.register(registerRequest)
                }
                if (registerResponse?.code() != 200) {
                    _uiMessageFlow.emit(context.getString(R.string.registration_failed))
                    _registerResult.emit(false)
                    return@launch
                }

                val loginRequest = LoginRequestDTO(email = email, password = password)
                val loginResponse = withContext(Dispatchers.IO) {
                    apiService.login(grantType = "password", loginRequest = loginRequest)
                }
                val authResponse: AuthResponseDTO? = loginResponse?.body()
                if (loginResponse?.code() == 200 && authResponse != null) {
                    preferences.authToken = authResponse.token
                    preferences.currentUserId = authResponse.user?.id
                    _uiMessageFlow.emit(context.getString(R.string.registration_successful))
                    _registerResult.emit(true)
                } else {
                    _uiMessageFlow.emit(context.getString(R.string.login_failed_after_registration))
                    _registerResult.emit(false)
                }
            } catch (ex: ConnectException) {
                _uiMessageFlow.emit(context.getString(R.string.connection_error))
                _registerResult.emit(false)
            } catch (ex: Exception) {
                _uiMessageFlow.emit("Unexpected error: ${ex.message}")
                Log.e("RegisterViewModel", "Unexpected error: ${ex.message}")
                _registerResult.emit(false)
            }
        }
    }
}
