package com.takanakonbu.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.takanakonbu.todoapp.ui.navigation.Screen
import com.takanakonbu.todoapp.ui.screens.AddTodoScreen
import com.takanakonbu.todoapp.ui.screens.EditTodoScreen
import com.takanakonbu.todoapp.ui.screens.TodoListScreen
import com.takanakonbu.todoapp.ui.theme.TodoAppTheme
import com.takanakonbu.todoapp.ui.viewmodel.TodoViewModel
import com.takanakonbu.todoapp.ui.viewmodel.TodoViewModelFactory

class MainActivity : ComponentActivity() {
    private val todoViewModel: TodoViewModel by viewModels {
        TodoViewModelFactory((application as TodoApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoAppTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.TodoList.route
                ) {
                    composable(Screen.TodoList.route) {
                        TodoListScreen(
                            viewModel = todoViewModel,
                            onNavigateToAdd = {
                                navController.navigate(Screen.AddTodo.route)
                            },
                            onNavigateToEdit = { todoId ->
                                navController.navigate(Screen.EditTodo.createRoute(todoId))
                            }
                        )
                    }
                    composable(Screen.AddTodo.route) {
                        AddTodoScreen(
                            viewModel = todoViewModel,
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable(
                        route = Screen.EditTodo.route,
                        arguments = listOf(
                            navArgument("todoId") { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        val todoId = backStackEntry.arguments?.getInt("todoId") ?: return@composable
                        EditTodoScreen(
                            todoId = todoId,
                            viewModel = todoViewModel,
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}