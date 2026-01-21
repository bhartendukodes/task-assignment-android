package com.wastesamaritan.task_assignment_android.events.presentation.create

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import com.wastesamaritan.task_assignment_android.core.ui.components.AppButton
import com.wastesamaritan.task_assignment_android.core.ui.components.AppTextField
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(
    onBackClick: () -> Unit,
    onEventCreated: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateEventViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()
    
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            snackbarHostState.showSnackbar("Event created successfully!")
            viewModel.resetState()
            onEventCreated()
        }
    }
    
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }
    
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = "Create Event",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Image Preview - Only compose when URL is valid and non-empty
            val isValidImageUrl = remember(uiState.imageUrl) {
                uiState.imageUrl.isNotBlank() && isValidUrl(uiState.imageUrl)
            }
            
            // Conditional composition - only compose when valid URL exists
            if (isValidImageUrl) {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Image Preview",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        coil.compose.AsyncImage(
                            model = uiState.imageUrl,
                            contentDescription = "Event image preview",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(MaterialTheme.shapes.large),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                    }
                }
            }
            
            // Event Title
            AppTextField(
                value = uiState.title,
                onValueChange = viewModel::updateTitle,
                label = "Event Title *",
                placeholder = "Enter event title",
                isError = uiState.titleError != null,
                errorMessage = uiState.titleError,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )
            
            // Description
            AppTextField(
                value = uiState.description,
                onValueChange = viewModel::updateDescription,
                label = "Description",
                placeholder = "Enter event description (optional)",
                singleLine = false,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )
            
            // Location
            AppTextField(
                value = uiState.location,
                onValueChange = viewModel::updateLocation,
                label = "Location *",
                placeholder = "Enter event location",
                isError = uiState.locationError != null,
                errorMessage = uiState.locationError,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )
            
            // Event Date
            AppTextField(
                value = uiState.eventDate,
                onValueChange = { },
                label = "Event Date & Time *",
                placeholder = "Tap to select date and time",
                isError = uiState.dateError != null,
                errorMessage = uiState.dateError,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading,
                readOnly = true,
                onClick = { showDatePicker = true },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select Date"
                        )
                    }
                }
            )
            
            // Total Slots
            AppTextField(
                value = uiState.totalSlots,
                onValueChange = viewModel::updateTotalSlots,
                label = "Total Slots *",
                placeholder = "Enter number of slots",
                keyboardType = KeyboardType.Number,
                isError = uiState.slotsError != null,
                errorMessage = uiState.slotsError,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )
            
            // Image URL
            AppTextField(
                value = uiState.imageUrl,
                onValueChange = viewModel::updateImageUrl,
                label = "Image URL",
                placeholder = "Enter image URL (optional)",
                keyboardType = KeyboardType.Uri,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Create Button
            AppButton(
                text = "Create Event",
                onClick = viewModel::createEvent,
                loading = uiState.isLoading,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        // Date Picker Dialog
        if (showDatePicker) {
            AlertDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { dateMillis ->
                                showDatePicker = false
                                showTimePicker = true
                            }
                        }
                    ) {
                        Text("Next")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDatePicker = false }
                    ) {
                        Text("Cancel")
                    }
                },
                text = {
                    DatePicker(state = datePickerState)
                }
            )
        }
        
        // Time Picker Dialog
        if (showTimePicker) {
            AlertDialog(
                onDismissRequest = { showTimePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { dateMillis ->
                                val selectedDate = Instant.ofEpochMilli(dateMillis)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                                
                                val selectedTime = LocalDateTime.of(
                                    selectedDate.year,
                                    selectedDate.monthValue,
                                    selectedDate.dayOfMonth,
                                    timePickerState.hour,
                                    timePickerState.minute
                                )
                                
                                val formattedDate = selectedTime.format(
                                    DateTimeFormatter.ISO_LOCAL_DATE_TIME
                                )
                                
                                viewModel.updateEventDate(formattedDate)
                                showTimePicker = false
                            }
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showTimePicker = false }
                    ) {
                        Text("Cancel")
                    }
                },
                text = {
                    TimePicker(state = timePickerState)
                }
            )
        }
    }
}

private fun isValidUrl(url: String): Boolean {
    return try {
        val urlPattern = android.util.Patterns.WEB_URL
        urlPattern.matcher(url).matches() || url.startsWith("http://") || url.startsWith("https://")
    } catch (e: Exception) {
        false
    }
}