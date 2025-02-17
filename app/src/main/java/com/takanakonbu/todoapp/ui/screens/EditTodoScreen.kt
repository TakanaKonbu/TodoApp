package com.takanakonbu.todoapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.takanakonbu.todoapp.data.TodoItem
import com.takanakonbu.todoapp.ui.viewmodel.TodoViewModel
import kotlinx.coroutines.flow.filterNotNull

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTodoScreen(
    todoId: Int,
    viewModel: TodoViewModel,
    onNavigateBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var currentTodo by remember { mutableStateOf<TodoItem?>(null) }

    // Flow to State conversion
    LaunchedEffect(todoId) {
        viewModel.getTodoById(todoId)
            .filterNotNull()
            .collect { todo ->
                currentTodo = todo
                title = todo.title
                content = todo.content
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Todo") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            currentTodo?.let {
                                viewModel.deleteTodo(it)
                                onNavigateBack()
                            }
                        }
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Todo")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                currentTodo?.let { todo ->
                    Button(
                        onClick = {
                            viewModel.toggleTodoComplete(todo)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (todo.isCompleted) "Mark Incomplete" else "Mark Complete")
                    }
                }

                Button(
                    onClick = {
                        currentTodo?.let {
                            viewModel.updateTodo(
                                it.copy(
                                    title = title,
                                    content = content
                                )
                            )
                            onNavigateBack()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = currentTodo != null && title.isNotBlank()
                ) {
                    Text("Save")
                }
            }
        }
    }
}