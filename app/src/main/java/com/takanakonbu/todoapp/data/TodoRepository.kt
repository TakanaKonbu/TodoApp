package com.takanakonbu.todoapp.data

import kotlinx.coroutines.flow.Flow

class TodoRepository(private val todoDao: TodoDao) {
    val allTodos: Flow<List<TodoItem>> = todoDao.getAllTodos()

    fun getTodoById(id: Int): Flow<TodoItem> {
        return todoDao.getTodoById(id)
    }

    suspend fun insertTodo(todoItem: TodoItem) {
        todoDao.insertTodo(todoItem)
    }

    suspend fun updateTodo(todoItem: TodoItem) {
        todoDao.updateTodo(todoItem)
    }

    suspend fun deleteTodo(todoItem: TodoItem) {
        todoDao.deleteTodo(todoItem)
    }
}