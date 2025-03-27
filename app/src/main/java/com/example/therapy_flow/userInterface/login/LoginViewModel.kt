package com.example.therapy_flow.userInterface.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.therapy_flow.R
import com.example.therapy_flow.network.ApiService
import com.example.therapy_flow.network.dtos.LoginRequestDTO
import com.example.therapy_flow.network.dtos.AuthResponseDTO
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
class LoginViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val apiService: ApiService,
    private val preferences: PreferencesManager
) : ViewModel() {

    private val _uiMessageFlow = MutableSharedFlow<String>()
    val uiMessageFlow = _uiMessageFlow.asSharedFlow()

    private val _navigateToMainFlow = MutableSharedFlow<Boolean>()
    val navigateToMainFlow = _navigateToMainFlow.asSharedFlow()

    fun performLogin(email: String, password: String) {
        val cleanEmail = email.trim()
        val cleanPassword = password.trim()

        if (cleanEmail.isEmpty() || cleanPassword.isEmpty()) {
            viewModelScope.launch {
                _uiMessageFlow.emit(appContext.getString(R.string.please_fill_in_all_fields))
            }
            return
        }

        loginUser(cleanEmail, cleanPassword)
    }

    private fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.login(
                        grantType = "password",
                        loginRequest = LoginRequestDTO(email = email, password = password)
                    )
                }

                var messageResId: Int? = null
                val responseBody: AuthResponseDTO? = response?.body()
                if (response == null) {
                    _uiMessageFlow.emit(appContext.getString(R.string.no_response_from_server))
                } else {
                    when (response.code()) {
                        200 -> {
                            preferences.authToken = responseBody?.token
                            preferences.currentUserId = responseBody?.user?.id
                            _navigateToMainFlow.emit(true)
                            _uiMessageFlow.emit(appContext.getString(R.string.login_successful))
                        }
                        304 -> messageResId = R.string.internal_error
                        400 -> messageResId = R.string.user_not_found_prompt
                        401 -> messageResId = R.string.incorrect_information
                        503 -> messageResId = R.string.service_unavailable
                        else -> messageResId = R.string.unexpected_error
                    }
                }

                messageResId?.let {
                    _uiMessageFlow.emit(appContext.getString(it))
                }
            } catch (ex: ConnectException) {
                _uiMessageFlow.emit(appContext.getString(R.string.connection_error))
            } catch (ex: Exception) {
                _uiMessageFlow.emit(appContext.getString(R.string.unexpected_error))
            }
        }
    }
}
