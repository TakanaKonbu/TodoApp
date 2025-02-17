package com.takanakonbu.todoapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.takanakonbu.todoapp.data.TodoItem
import com.takanakonbu.todoapp.data.TodoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {
    // UIの状態を保持
    val allTodos: StateFlow<List<TodoItem>> = repository.allTodos.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // 特定のTodoを取得
    fun getTodoById(id: Int): StateFlow<TodoItem?> {
        return repository.getTodoById(id).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    }

    // Todoの追加
    fun addTodo(title: String, content: String) {
        viewModelScope.launch {
            val todoItem = TodoItem(
                title = title,
                content = content
            )
            repository.insertTodo(todoItem)
        }
    }

    // Todoの更新
    fun updateTodo(todoItem: TodoItem) {
        viewModelScope.launch {
            repository.updateTodo(todoItem)
        }
    }

    // Todoの削除
    fun deleteTodo(todoItem: TodoItem) {
        viewModelScope.launch {
            repository.deleteTodo(todoItem)
        }
    }

    // 完了状態の切り替え
    fun toggleTodoComplete(todoItem: TodoItem) {
        viewModelScope.launch {
            repository.updateTodo(todoItem.copy(isCompleted = !todoItem.isCompleted))
        }
    }
}

// ViewModelファクトリ
class TodoViewModelFactory(private val repository: TodoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}