package com.wastesamaritan.task_assignment_android.events.presentation.details

import com.wastesamaritan.task_assignment_android.events.domain.entities.Event

sealed class EventDetailsUiState {
    object Loading : EventDetailsUiState()
    data class Success(val event: Event) : EventDetailsUiState()
    data class Error(val message: String) : EventDetailsUiState()
}