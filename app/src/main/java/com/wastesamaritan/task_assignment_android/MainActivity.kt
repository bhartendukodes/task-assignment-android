package com.wastesamaritan.task_assignment_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.rememberNavController
import com.wastesamaritan.task_assignment_android.core.ui.theme.TaskAssignmentTheme
import com.wastesamaritan.task_assignment_android.navigation.MainViewModel
import com.wastesamaritan.task_assignment_android.navigation.Screen
import com.wastesamaritan.task_assignment_android.navigation.TaskAssignmentNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskAssignmentTheme {
                val navController = rememberNavController()
                val mainViewModel: MainViewModel = hiltViewModel()
                val isLoggedIn by mainViewModel.isLoggedIn.collectAsState()
                
                // Determine start destination immediately
                val startDestination = remember(isLoggedIn) {
                    if (isLoggedIn) Screen.Main.route else Screen.Login.route
                }
                
                TaskAssignmentNavigation(
                    navController = navController,
                    startDestination = startDestination,
                    mainViewModel = mainViewModel
                )
            }
        }
    }
}