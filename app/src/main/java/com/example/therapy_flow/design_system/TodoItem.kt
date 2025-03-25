package com.example.therapy_flow.design_system

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.therapy_flow.network.dtos.TodoDTO

@OptIn(ExperimentalMaterial3Api::class) // Pour ExposedDropdownMenuBox
@Composable
fun TodoItem(
    todo: TodoDTO,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onUpdateStatus: (String) -> Unit
) {
    // Liste de statuts possibles (à adapter à ton cas).
    val statuses = listOf("TODO", "IN_PROGRESS", "DONE")

    // État local pour la Dropdown
    var expanded by remember { mutableStateOf(false) }
    var selectedStatus by remember(todo.id) { mutableStateOf(todo.status) }

    Card(
        modifier = Modifier
            .fillMaxWidth()              // Occuper toute la largeur
            .padding(vertical = 4.dp),   // Marges verticales pour « aérer »
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            // 1) Titre
            Text(
                text = todo.taskName,
                style = MaterialTheme.typography.titleSmall
            )
            // 2) Description
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = todo.description ?: "No description",
                style = MaterialTheme.typography.bodyMedium
            )
            // 3) Statut (affiché aussi en dur, en plus du spinner plus bas si tu veux)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Status: ${todo.status}",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Ligne d’icônes + menu de sélection du statut
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Icône pour supprimer
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete Todo"
                    )
                }
                // Icône pour éditer
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit Todo"
                    )
                }
                // Spinner (menu déroulant) pour modifier le statut
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = selectedStatus,
                        onValueChange = {},
                        label = { Text("Status") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expanded
                            )
                        },
                        modifier = Modifier
                            .menuAnchor() // pour la bonne position du menu
                            .widthIn(min = 100.dp) // Largeur mini pour le champ
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        statuses.forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status) },
                                onClick = {
                                    selectedStatus = status
                                    onUpdateStatus(status) // callback parent
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
