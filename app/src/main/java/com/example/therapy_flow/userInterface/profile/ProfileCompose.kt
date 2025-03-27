package com.example.therapy_flow.userInterface.profile

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.therapy_flow.design_system.MyTextField
import com.example.therapy_flow.utils.Therapist

@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel
) {
    val therapist by viewModel.therapistDataFlow.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiMessageFlow.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    ProfileContent(
        therapist = therapist,
        onUpdate = { updatedTherapist ->
            viewModel.updateTherapist(updatedTherapist)
        }
    )
}

@Composable
fun ProfileContent(
    therapist: Therapist? = null,
    onUpdate: (Therapist) -> Unit = {}
) {
    var firstName by remember { mutableStateOf(therapist?.firstName ?: "") }
    var lastName by remember { mutableStateOf(therapist?.lastName ?: "") }
    var email by remember { mutableStateOf(therapist?.email ?: "") }

    LaunchedEffect(therapist) {
        therapist?.let {
            firstName = it.firstName
            lastName = it.lastName
            email = it.email
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Text(
            text = "Complete your infos",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Champ Prénom
        MyTextField(
            value = firstName,
            onValueChange = { firstName = it },
            hint = "First Name",
            hintColor = MaterialTheme.colorScheme.onSurfaceVariant,
            leadingIcon = Icons.Outlined.Person
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Champ Nom
        MyTextField(
            value = lastName,
            onValueChange = { lastName = it },
            hint = "Last Name",
            hintColor = MaterialTheme.colorScheme.onSurfaceVariant,
            leadingIcon = Icons.Outlined.Person
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Champ Email
        MyTextField(
            value = email,
            onValueChange = { email = it },
            hint = "Email",
            hintColor = MaterialTheme.colorScheme.onSurfaceVariant,
            leadingIcon = Icons.Outlined.Email
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                onUpdate(
                    Therapist(
                        id = therapist?.id ?: "",
                        firstName = firstName,
                        lastName = lastName,
                        email = email
                    )
                )
            },
            modifier = Modifier.wrapContentWidth()
        ) {
            Text(text = "Update Profile")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    ProfileContent(
        therapist = Therapist(
            id = "1234",
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com"
        )
    )
}
