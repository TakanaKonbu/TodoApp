package com.takanakonbu.todoapp

import android.app.Application
import com.takanakonbu.todoapp.data.TodoDatabase
import com.takanakonbu.todoapp.data.TodoRepository

class TodoApplication : Application() {
    private val database by lazy { TodoDatabase.getDatabase(this) }
    val repository by lazy { TodoRepository(database.todoDao()) }
}