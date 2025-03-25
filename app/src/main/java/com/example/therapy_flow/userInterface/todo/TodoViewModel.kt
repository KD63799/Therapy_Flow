package com.example.therapy_flow.userInterface.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.therapy_flow.network.ApiService
import com.example.therapy_flow.network.dtos.CreateTodoDTO
import com.example.therapy_flow.network.dtos.TodoDTO
import com.example.therapy_flow.network.dtos.UpdateTodoDTO
import com.example.therapy_flow.utils.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val preferences: PreferencesManager,
    private val apiService: ApiService
) : ViewModel() {

    // État contenant la liste des tâches récupérées depuis l'API.
    private val _todoList = MutableStateFlow<List<TodoDTO>>(emptyList())
    val todoList: StateFlow<List<TodoDTO>> = _todoList

    init {
        fetchTodos()
    }

    fun fetchTodos() {
        viewModelScope.launch {
            try {
                val therapistId = preferences.currentUserId ?: return@launch
                val response = withContext(Dispatchers.IO) { apiService.getTodos() }
                val todos = response?.body() ?: emptyList()
                _todoList.value = todos.filter { it.therapistId == therapistId }
            } catch (e: Exception) {
                // Gérer l'erreur (par exemple via un message toast)
                e.printStackTrace()
            }
        }
    }

    fun createTodo(taskName: String, description: String?) {
        viewModelScope.launch {
            try {
                val therapistId = preferences.currentUserId ?: return@launch
                val newTodo = CreateTodoDTO(
                    taskName = taskName,
                    description = description,
                    status = "PENDING",
                    therapistId = therapistId
                )
                withContext(Dispatchers.IO) { apiService.createTodo(newTodo) }
                fetchTodos()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateTodo(todoId: String, taskName: String? = null, description: String? = null, status: String? = null) {
        viewModelScope.launch {
            try {
                val updateDto = UpdateTodoDTO(
                    taskName = taskName,
                    description = description,
                    status = status
                )
                withContext(Dispatchers.IO) { apiService.updateTodo(todoId, updateDto) }
                fetchTodos()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteTodo(todoId: String) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) { apiService.deleteTodo(todoId) }
                fetchTodos()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
