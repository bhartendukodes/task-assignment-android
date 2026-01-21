package com.wastesamaritan.task_assignment_android.bookings.domain.repository

import com.wastesamaritan.task_assignment_android.bookings.domain.entities.Booking
import com.wastesamaritan.task_assignment_android.core.network.ApiResult

interface BookingRepository {
    suspend fun createBooking(eventId: Int): ApiResult<Booking>
    suspend fun getMyBookings(): ApiResult<List<Booking>>
}