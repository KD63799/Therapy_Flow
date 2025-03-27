package com.example.therapy_flow.userInterface.landing

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.therapy_flow.R
import com.example.therapy_flow.utils.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandingViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val preferences: PreferencesManager,
) : ViewModel() {

    private val _navigateToLoginFlow = MutableSharedFlow<Boolean>()
    val navigateToLoginFlow = _navigateToLoginFlow.asSharedFlow()

    private val _uiMessageFlow = MutableSharedFlow<String>()
    val uiMessageFlow = _uiMessageFlow.asSharedFlow()

    fun disconnectUser() {
        preferences.authToken = null
        preferences.currentUserId = null

        viewModelScope.launch {
            _uiMessageFlow.emit(appContext.getString(R.string.logout_successful))
            _navigateToLoginFlow.emit(true)
        }
    }
}
