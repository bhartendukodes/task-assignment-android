package com.wastesamaritan.task_assignment_android.bookings.data.repository

import com.wastesamaritan.task_assignment_android.bookings.data.dto.CreateBookingRequest
import com.wastesamaritan.task_assignment_android.bookings.data.mappers.toDomain
import com.wastesamaritan.task_assignment_android.bookings.domain.entities.Booking
import com.wastesamaritan.task_assignment_android.bookings.domain.repository.BookingRepository
import com.wastesamaritan.task_assignment_android.core.network.ApiResult
import com.wastesamaritan.task_assignment_android.core.network.ApiService
import com.wastesamaritan.task_assignment_android.core.network.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookingRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : BookingRepository {
    
    override suspend fun createBooking(eventId: Int): ApiResult<Booking> {
        return safeApiCall {
            val response = apiService.createBooking(CreateBookingRequest(eventId))
            response.toDomain()
        }
    }
    
    override suspend fun getMyBookings(): ApiResult<List<Booking>> {
        return safeApiCall {
            apiService.getMyBookings().map { it.toDomain() }
        }
    }
}