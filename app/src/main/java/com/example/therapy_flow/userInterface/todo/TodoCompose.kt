package com.example.therapy_flow.userInterface.todo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.therapy_flow.design_system.TodoItem
import com.example.therapy_flow.network.dtos.TodoDTO

@Composable
fun TodoScreen(
    navController: NavHostController,
    viewModel: TodoViewModel = hiltViewModel()
) {
    // Collecte de la liste des tâches depuis le ViewModel
    val todos by viewModel.todoList.collectAsState()

    // Lancement d'un effet pour rafraîchir la liste à l'ouverture
    LaunchedEffect(Unit) {
        viewModel.fetchTodos()
    }

    // TodoScreen gère toute la logique et passe uniquement les callbacks et les états à TodoContent
    TodoContent(
        todos = todos,
        onCreateTodo = { taskName, description ->
            viewModel.createTodo(taskName, description)
        },
        onDeleteTodo = { todoId ->
            viewModel.deleteTodo(todoId)
        },
        onUpdateTodoStatus = { todoId, newStatus ->
            viewModel.updateTodo(todoId, status = newStatus)
        }
    )
}

@Composable
fun TodoContent(
    todos: List<TodoDTO>,
    onCreateTodo: (String, String?) -> Unit,
    onDeleteTodo: (String) -> Unit,
    onUpdateTodoStatus: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var newTaskName by remember { mutableStateOf("") }
    var newTaskDescription by remember { mutableStateOf("") }

    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(text = "My Todo List", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        // Formulaire pour ajouter une nouvelle tâche
        OutlinedTextField(
            value = newTaskName,
            onValueChange = { newTaskName = it },
            label = { Text("Task Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = newTaskDescription,
            onValueChange = { newTaskDescription = it },
            label = { Text("Task Description") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (newTaskName.isNotBlank()) {
                    onCreateTodo(newTaskName, newTaskDescription)
                    newTaskName = ""
                    newTaskDescription = ""
                }
            },
            modifier = Modifier
                .wrapContentWidth()
        ) {
            Text(text = "Add Task")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Your Tasks:", style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(8.dp))
        // Affichage des tâches dans une LazyRow
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(todos) { todo ->
                TodoItem(
                    todo = todo,
                    onDelete = { onDeleteTodo(todo.id) },
                    onUpdateStatus = { newStatus -> onUpdateTodoStatus(todo.id, newStatus) },
                    onEdit = {}
                )
            }
        }
    }
}
