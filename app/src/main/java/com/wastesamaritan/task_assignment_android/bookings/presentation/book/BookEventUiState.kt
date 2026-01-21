package com.wastesamaritan.task_assignment_android.bookings.presentation.book

data class BookEventUiState(
    val isLoading: Boolean = false,
    val isBookingSuccessful: Boolean = false,
    val error: String? = null
)