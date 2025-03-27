package com.example.therapy_flow.design_system

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.therapy_flow.utils.Patient


@Composable
fun PatientItem(
    patient: Patient,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("${patient.firstName} ${patient.lastName}",
                style = MaterialTheme.typography.bodyLarge)
            Text(patient.email,
                style = MaterialTheme.typography.bodyMedium)
            Text("Birthdate: ${patient.birthdate}",
                style = MaterialTheme.typography.bodySmall)
            Text("Phone: ${patient.phone}",
                style = MaterialTheme.typography.bodySmall)
        }
        Icon(
            imageVector = Icons.Outlined.Edit,
            contentDescription = "Edit Patient",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(36.dp)
                .clickable { onEdit() }
                .padding(4.dp)
        )
        Icon(
            imageVector = Icons.Outlined.Delete,
            contentDescription = "Delete Patient",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(36.dp)
                .clickable { onDelete() }
                .padding(4.dp)
        )
    }
}