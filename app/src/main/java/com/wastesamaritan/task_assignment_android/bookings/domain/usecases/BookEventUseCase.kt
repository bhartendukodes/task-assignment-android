package com.wastesamaritan.task_assignment_android.bookings.domain.usecases

import com.wastesamaritan.task_assignment_android.bookings.domain.entities.Booking
import com.wastesamaritan.task_assignment_android.bookings.domain.repository.BookingRepository
import com.wastesamaritan.task_assignment_android.core.network.ApiResult
import javax.inject.Inject

class BookEventUseCase @Inject constructor(
    private val bookingRepository: BookingRepository
) {
    suspend operator fun invoke(eventId: Int): ApiResult<Booking> {
        return when {
            eventId <= 0 -> ApiResult.Error(IllegalArgumentException("Invalid event ID"))
            else -> bookingRepository.createBooking(eventId)
        }
    }
}