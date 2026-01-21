package com.wastesamaritan.task_assignment_android.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.wastesamaritan.task_assignment_android.bookings.presentation.list.MyBookingsScreen
import com.wastesamaritan.task_assignment_android.events.presentation.list.EventsScreen
import com.wastesamaritan.task_assignment_android.events.presentation.top.TopEventsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onLogout: () -> Unit,
    onEventClick: (Int) -> Unit,
    onCreateEventClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        TabItem("Events", Icons.Default.DateRange),
        TabItem("Top Events", Icons.Default.Star),
        TabItem("My Bookings", Icons.Default.FavoriteBorder)
    )
    
    // Get ViewModels for refresh
    val eventsViewModel: com.wastesamaritan.task_assignment_android.events.presentation.list.EventsViewModel = hiltViewModel()
    val bookingsViewModel: com.wastesamaritan.task_assignment_android.bookings.presentation.list.MyBookingsViewModel = hiltViewModel()
    
    // Refresh bookings when My Bookings tab is selected
    LaunchedEffect(selectedTabIndex) {
        if (selectedTabIndex == 2) {
            bookingsViewModel.refresh()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = tabs[selectedTabIndex].title,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Logout"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title
                            )
                        },
                        label = {
                            Text(text = tab.title)
                        }
                    )
                }
            }
        },
        modifier = modifier
    ) { paddingValues ->
        when (selectedTabIndex) {
            0 -> EventsScreen(
                onEventClick = onEventClick,
                onCreateEventClick = onCreateEventClick,
                viewModel = eventsViewModel,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
            1 -> TopEventsScreen(
                onEventClick = onEventClick,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
            2 -> MyBookingsScreen(
                viewModel = bookingsViewModel,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        }
    }
}

private data class TabItem(
    val title: String,
    val icon: ImageVector
)