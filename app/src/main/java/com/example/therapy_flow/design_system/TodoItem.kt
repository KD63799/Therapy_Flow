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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoItem(
    todo: TodoDTO,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onUpdateStatus: (String) -> Unit
) {
    val statuses = listOf("TODO", "IN_PROGRESS", "DONE")

    var expanded by remember { mutableStateOf(false) }
    var selectedStatus by remember(todo.id) { mutableStateOf(todo.status) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = todo.taskName,
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = todo.description ?: "No description",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Status: ${todo.status}",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete Todo"
                    )
                }

                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit Todo"
                    )
                }

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
                            .menuAnchor()
                            .widthIn(min = 100.dp)
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
                                    onUpdateStatus(status)
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
