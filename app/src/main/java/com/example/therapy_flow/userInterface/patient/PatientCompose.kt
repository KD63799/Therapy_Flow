package com.example.therapy_flow.userInterface.patient

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GroupAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.therapy_flow.R
import com.example.therapy_flow.design_system.Patient
import com.example.therapy_flow.design_system.PatientItem
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PatientScreen(
    navController: NavHostController,
    viewModel: PatientViewModel = hiltViewModel()
) {
    // Récupération de la liste des patients et de l'état du formulaire depuis le ViewModel
    val patients by viewModel.patients.collectAsState(initial = emptyList())
    val isFormExpanded by viewModel.isFormExpanded.collectAsState()

    // Collecte des messages toast depuis le ViewModel
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.toastMessageFlow.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    PatientContent(
        patients = patients,
        isFormExpanded = isFormExpanded,
        onToggleForm = { viewModel.toggleForm() },
        onAddPatient = { firstName, lastName, email, birthdate, phone ->
            viewModel.addPatient(firstName, lastName, email, birthdate, phone)
        },
        onEditPatient = { patient ->
            viewModel.updatePatient(patient)
        },
        onDeletePatient = { patientId ->
            viewModel.deletePatient(patientId)
        },
        modifier = Modifier
    )
}

@Composable
fun PatientContent(
    patients: List<Patient>,
    isFormExpanded: Boolean,
    onToggleForm: () -> Unit,
    onAddPatient: (String, String, String, String, String) -> Unit,
    onEditPatient: (Patient) -> Unit,
    onDeletePatient: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // États locaux pour les champs du formulaire
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggleForm() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.add_new_patient),
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                imageVector = Icons.Outlined.GroupAdd,
                contentDescription = stringResource(R.string.expand_patient_form),
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedVisibility(
            visible = isFormExpanded,
            enter = fadeIn(animationSpec = tween(durationMillis = 400)) +
                    expandVertically(animationSpec = tween(durationMillis = 400)),
            exit = fadeOut(animationSpec = tween(durationMillis = 200)) +
                    slideOutVertically(animationSpec = tween(durationMillis = 250))
        ) {
            Column {
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = birthdate,
                    onValueChange = { birthdate = it },
                    label = { Text("Birthdate (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        onAddPatient(firstName, lastName, email, birthdate, phone)
                        // Réinitialisation des champs après ajout
                        firstName = ""
                        lastName = ""
                        email = ""
                        birthdate = ""
                        phone = ""
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .wrapContentWidth()
                ) {
                    Text("Add Patient", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Divider()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Patient List", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Column {
            patients.forEach { patient ->
                PatientItem(
                    patient = patient,
                    onEdit = { onEditPatient(patient) },
                    onDelete = { onDeletePatient(patient.id) }
                )
                Divider()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PatientContentPreview() {
    // Exemple de liste de patients pour la preview
    val samplePatients = listOf(
        Patient("1", "John", "Doe", "john@example.com", "1990-01-01", "1234567890"),
        Patient("2", "Jane", "Doe", "jane@example.com", "1992-02-02", "0987654321")
    )
    PatientContent(
        patients = samplePatients,
        isFormExpanded = true,
        onToggleForm = {},
        onAddPatient = { _, _, _, _, _ -> },
        onEditPatient = {},
        onDeletePatient = {},
        modifier = Modifier
    )
}
