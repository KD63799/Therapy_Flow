package com.example.therapy_flow.userInterface.splash

import com.example.therapy_flow.utils.PreferencesManager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _goToDestination = MutableSharedFlow<Boolean>()
    val goToDestination = _goToDestination.asSharedFlow()

    init {
        checkToken()
    }

    private fun checkToken() {
        viewModelScope.launch {
            delay(4000L)
            val token = preferencesManager.authToken
            _goToDestination.emit(token != null)
        }
    }
}

