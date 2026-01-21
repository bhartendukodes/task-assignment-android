package com.wastesamaritan.task_assignment_android.events.presentation.top

import com.wastesamaritan.task_assignment_android.events.domain.entities.Event

sealed class TopEventsUiState {
    object Loading : TopEventsUiState()
    data class Success(val events: List<Event>) : TopEventsUiState()
    data class Error(val message: String) : TopEventsUiState()
}