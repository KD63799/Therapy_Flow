package com.example.therapy_flow.userInterface.schedule

import androidx.lifecycle.ViewModel
import com.example.therapy_flow.network.ApiService
import com.example.therapy_flow.utils.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val preferences: PreferencesManager,
    private val apiService: ApiService
) : ViewModel() {

}