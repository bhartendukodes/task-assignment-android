package com.wastesamaritan.task_assignment_android.events.presentation.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import coil.compose.AsyncImage
import com.wastesamaritan.task_assignment_android.bookings.presentation.book.BookEventViewModel
import com.wastesamaritan.task_assignment_android.core.ui.components.AppButton
import com.wastesamaritan.task_assignment_android.core.ui.components.ErrorMessage
import com.wastesamaritan.task_assignment_android.core.ui.components.LoadingIndicator
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsScreen(
    onBackClick: () -> Unit,
    onBookEvent: (Int) -> Unit,
    onBookingSuccess: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: EventDetailsViewModel = hiltViewModel(),
    bookEventViewModel: BookEventViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val bookingState by bookEventViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(bookingState.error) {
        bookingState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            bookEventViewModel.clearError()
        }
    }
    
    LaunchedEffect(bookingState.isBookingSuccessful) {
        if (bookingState.isBookingSuccessful) {
            snackbarHostState.showSnackbar("Event booked successfully!")
            // Refresh event details and trigger parent refresh
            viewModel.refresh()
            onBookingSuccess()
        }
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Event Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        modifier = modifier
    ) { paddingValues ->
        when (val state = uiState) {
            is EventDetailsUiState.Loading -> {
                LoadingIndicator(
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            is EventDetailsUiState.Success -> {
                val event = state.event
                
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Event Image
                    AsyncImage(
                        model = event.imageUrl ?: "https://via.placeholder.com/400x300",
                        contentDescription = "Event image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Crop
                    )
                    
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // Title
                        Text(
                            text = event.title,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Location
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = event.location,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Date
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Date",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = formatEventDate(event.eventDate),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Available Slots
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Available slots",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "${event.availableSlots ?: event.totalSlots} of ${event.totalSlots} slots available",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Description
                        if (!event.description.isNullOrBlank()) {
                            Text(
                                text = "Description",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = event.description,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                        
                        // Book Event Button
                        AppButton(
                            text = if (bookingState.isBookingSuccessful) "Booked Successfully!" else "Book Event",
                            onClick = { bookEventViewModel.bookEvent() },
                            enabled = (event.availableSlots ?: 0) > 0 && !bookingState.isBookingSuccessful,
                            loading = bookingState.isLoading,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            
            is EventDetailsUiState.Error -> {
                ErrorMessage(
                    message = state.message,
                    onRetry = viewModel::refresh,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }
        }
    }
}

private fun formatEventDate(dateString: String): String {
    return try {
        val dateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        dateTime.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy 'at' HH:mm"))
    } catch (e: Exception) {
        dateString
    }
}