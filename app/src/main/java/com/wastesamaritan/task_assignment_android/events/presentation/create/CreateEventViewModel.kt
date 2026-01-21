package com.wastesamaritan.task_assignment_android.events.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wastesamaritan.task_assignment_android.core.network.ApiResult
import com.wastesamaritan.task_assignment_android.events.domain.usecases.CreateEventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class CreateEventViewModel @Inject constructor(
    private val createEventUseCase: CreateEventUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CreateEventUiState())
    val uiState: StateFlow<CreateEventUiState> = _uiState.asStateFlow()
    
    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(
            title = title,
            titleError = null,
            error = null
        )
    }
    
    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(
            description = description,
            error = null
        )
    }
    
    fun updateLocation(location: String) {
        _uiState.value = _uiState.value.copy(
            location = location,
            locationError = null,
            error = null
        )
    }
    
    fun updateEventDate(date: String) {
        _uiState.value = _uiState.value.copy(
            eventDate = date,
            dateError = null,
            error = null
        )
    }
    
    fun updateTotalSlots(slots: String) {
        _uiState.value = _uiState.value.copy(
            totalSlots = slots,
            slotsError = null,
            error = null
        )
    }
    
    fun updateImageUrl(imageUrl: String) {
        _uiState.value = _uiState.value.copy(
            imageUrl = imageUrl,
            error = null
        )
    }
    
    fun createEvent() {
        val currentState = _uiState.value
        
        // Validate inputs
        var hasError = false
        val titleError = if (currentState.title.isBlank()) "Title is required" else null
        val locationError = if (currentState.location.isBlank()) "Location is required" else null
        val dateError = if (currentState.eventDate.isBlank()) "Event date is required" else null
        val slotsError = when {
            currentState.totalSlots.isBlank() -> "Total slots is required"
            currentState.totalSlots.toIntOrNull() == null -> "Invalid number"
            currentState.totalSlots.toInt() <= 0 -> "Slots must be greater than 0"
            else -> null
        }
        
        if (titleError != null || locationError != null || dateError != null || slotsError != null) {
            _uiState.value = currentState.copy(
                titleError = titleError,
                locationError = locationError,
                dateError = dateError,
                slotsError = slotsError
            )
            return
        }
        
        _uiState.value = currentState.copy(
            isLoading = true,
            error = null,
            titleError = null,
            locationError = null,
            dateError = null,
            slotsError = null
        )
        
        viewModelScope.launch {
            when (val result = createEventUseCase(
                title = currentState.title.trim(),
                description = currentState.description.trim().takeIf { it.isNotBlank() },
                location = currentState.location.trim(),
                eventDate = currentState.eventDate,
                totalSlots = currentState.totalSlots.toInt(),
                imageUrl = currentState.imageUrl.trim().takeIf { it.isNotBlank() }
            )) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                }
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.exception.message ?: "Failed to create event"
                    )
                }
                is ApiResult.Loading -> {
                    // Already handled above
                }
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun resetState() {
        _uiState.value = CreateEventUiState()
    }
}