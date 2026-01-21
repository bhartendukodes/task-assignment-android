package com.wastesamaritan.task_assignment_android.events.presentation.list

import com.wastesamaritan.task_assignment_android.events.domain.entities.Event

sealed class EventsUiState {
    object Loading : EventsUiState()
    data class Success(val events: List<Event>) : EventsUiState()
    data class Error(val message: String) : EventsUiState()
}