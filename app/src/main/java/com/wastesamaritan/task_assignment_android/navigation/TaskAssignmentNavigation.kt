package com.wastesamaritan.task_assignment_android.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wastesamaritan.task_assignment_android.auth.presentation.login.LoginScreen
import com.wastesamaritan.task_assignment_android.auth.presentation.register.RegisterScreen
import com.wastesamaritan.task_assignment_android.bookings.presentation.list.MyBookingsScreen
import com.wastesamaritan.task_assignment_android.events.presentation.create.CreateEventScreen
import com.wastesamaritan.task_assignment_android.events.presentation.details.EventDetailsScreen
import com.wastesamaritan.task_assignment_android.events.presentation.list.EventsScreen
import com.wastesamaritan.task_assignment_android.events.presentation.top.TopEventsScreen

@Composable
fun TaskAssignmentNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val uiState by mainViewModel.uiState.collectAsState()
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Auth screens
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }
        
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegistrationSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
        
        // Main app screens
        composable(Screen.Main.route) {
            val eventsViewModel: com.wastesamaritan.task_assignment_android.events.presentation.list.EventsViewModel = hiltViewModel()
            MainScreen(
                onLogout = {
                    mainViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                },
                onEventClick = { eventId ->
                    navController.navigate(Screen.EventDetails.createRoute(eventId))
                },
                onCreateEventClick = {
                    navController.navigate(Screen.CreateEvent.route)
                }
            )
        }
        
        composable(Screen.Events.route) {
            val eventsViewModel: com.wastesamaritan.task_assignment_android.events.presentation.list.EventsViewModel = hiltViewModel()
            EventsScreen(
                onEventClick = { eventId ->
                    navController.navigate(Screen.EventDetails.createRoute(eventId))
                },
                onCreateEventClick = {
                    navController.navigate(Screen.CreateEvent.route)
                },
                viewModel = eventsViewModel
            )
        }
        
        composable(Screen.TopEvents.route) {
            TopEventsScreen(
                onEventClick = { eventId ->
                    navController.navigate(Screen.EventDetails.createRoute(eventId))
                }
            )
        }
        
        composable(Screen.MyBookings.route) {
            MyBookingsScreen()
        }
        
        composable(Screen.CreateEvent.route) {
            val eventsViewModel: com.wastesamaritan.task_assignment_android.events.presentation.list.EventsViewModel = hiltViewModel()
            CreateEventScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onEventCreated = {
                    // Refresh events list before navigating back
                    eventsViewModel.refresh()
                    navController.popBackStack()
                }
            )
        }
        
        composable(
            route = Screen.EventDetails.route,
            arguments = Screen.EventDetails.arguments
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getInt("eventId") ?: 0
            val eventsViewModel: com.wastesamaritan.task_assignment_android.events.presentation.list.EventsViewModel = hiltViewModel()
            val bookingsViewModel: com.wastesamaritan.task_assignment_android.bookings.presentation.list.MyBookingsViewModel = hiltViewModel()
            
            EventDetailsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onBookEvent = { eventId ->
                    // Handle booking logic through ViewModel
                    // Show success/error message
                },
                onBookingSuccess = {
                    // Refresh both events and bookings lists after successful booking
                    eventsViewModel.refresh()
                    bookingsViewModel.refresh()
                }
            )
        }
    }
}