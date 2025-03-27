package com.example.therapy_flow.userInterface.schedule

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.therapy_flow.R
import com.example.therapy_flow.design_system.ConsultationItem
import com.example.therapy_flow.utils.Consultation

@Composable
fun ScheduleScreen(
    navController: NavHostController,
    viewModel: ScheduleViewModel = hiltViewModel()
) {
    val consultations by viewModel.consultationsStateFlow.collectAsState()
    LaunchedEffect(Unit) {
    }
    ScheduleContent(consultations = consultations)
}

@Composable
fun ScheduleContent(consultations: List<Consultation>) {
    if (consultations.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = stringResource(R.string.you_don_t_have_an_appointment_scheduled))
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(consultations) { consultation ->
                ConsultationItem(consultation = consultation) {
                }
            }
        }
    }
}
