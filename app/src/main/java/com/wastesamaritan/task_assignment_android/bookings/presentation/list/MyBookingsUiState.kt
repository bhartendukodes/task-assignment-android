package com.wastesamaritan.task_assignment_android.bookings.presentation.list

import com.wastesamaritan.task_assignment_android.bookings.domain.entities.Booking

sealed class MyBookingsUiState {
    object Loading : MyBookingsUiState()
    data class Success(val bookings: List<Booking>) : MyBookingsUiState()
    data class Error(val message: String) : MyBookingsUiState()
}