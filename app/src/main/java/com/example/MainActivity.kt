package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.auth.AuthViewModel
import com.example.ui.auth.SignUpScreen
import com.example.ui.task.TaskDashboardScreen
import com.example.ui.task.TaskViewModel
import com.example.ui.theme.TaskSecureTheme

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TaskSecureTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TaskSecureApp(
                        authViewModel = authViewModel,
                        taskViewModel = taskViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun TaskSecureApp(
    authViewModel: AuthViewModel,
    taskViewModel: TaskViewModel
) {
    val currentUser by authViewModel.currentUser.collectAsState()

    if (currentUser != null && currentUser?.isLoggedIn == true) {
        TaskDashboardScreen(
            currentUser = currentUser,
            taskViewModel = taskViewModel,
            onLogout = { authViewModel.logout() }
        )
    } else {
        SignUpScreen(
            viewModel = authViewModel,
            onAuthSuccess = {
                // Flow automatically transitions as currentUser state updates
            }
        )
    }
}

