package com.wastesamaritan.task_assignment_android.events.presentation.create

data class CreateEventUiState(
    val title: String = "",
    val description: String = "",
    val location: String = "",
    val eventDate: String = "",
    val totalSlots: String = "",
    val imageUrl: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val titleError: String? = null,
    val locationError: String? = null,
    val dateError: String? = null,
    val slotsError: String? = null
)