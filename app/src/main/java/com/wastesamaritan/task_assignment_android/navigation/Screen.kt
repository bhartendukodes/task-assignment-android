package com.wastesamaritan.task_assignment_android.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    
    object Login : Screen("login")
    object Register : Screen("register")
    object Main : Screen("main")
    object Events : Screen("events")
    object TopEvents : Screen("top_events")
    object MyBookings : Screen("my_bookings")
    object CreateEvent : Screen("create_event")
    
    object EventDetails : Screen(
        route = "event_details/{eventId}",
        arguments = listOf(
            navArgument("eventId") {
                type = NavType.IntType
            }
        )
    ) {
        fun createRoute(eventId: Int): String = "event_details/$eventId"
    }
}