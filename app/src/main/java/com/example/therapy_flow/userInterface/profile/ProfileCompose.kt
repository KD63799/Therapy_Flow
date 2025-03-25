package com.example.therapy_flow.userInterface.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
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
    val therapist by viewModel.therapistData.collectAsState()
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
    var name by remember { mutableStateOf(therapist?.name ?: "") }
    var email by remember { mutableStateOf(therapist?.email ?: "") }

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
        Spacer(modifier = Modifier.height(100.dp))

        Column {
            MyTextField(
                value = name,
                onValueChange = { name = it },
                hint = "Nom complet",
                hintColor = MaterialTheme.colorScheme.onSurfaceVariant,
                leadingIcon = Icons.Outlined.Person
            )
            Spacer(modifier = Modifier.height(16.dp))

            MyTextField(
                value = email,
                onValueChange = { email = it },
                hint = "Email",
                hintColor = MaterialTheme.colorScheme.onSurfaceVariant,
                leadingIcon = Icons.Outlined.Email
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(100.dp))

        Button(
            onClick = {
                onUpdate(
                    Therapist(
                        id = therapist?.id ?: "",
                        name = name,
                        email = email
                    )
                )
            },
            modifier = Modifier
                .wrapContentWidth()
        ) {
            Text(text = "Mettre à jour le profil")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    ProfileContent(
        therapist = Therapist(
            id = "1234",
            name = "John Doe",
            email = "john.doe@example.com"
        )
    )
}
