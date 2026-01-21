package com.wastesamaritan.task_assignment_android.core.network

import com.wastesamaritan.task_assignment_android.auth.data.dto.LoginRequest
import com.wastesamaritan.task_assignment_android.auth.data.dto.RefreshTokenRequest
import com.wastesamaritan.task_assignment_android.auth.data.dto.RegisterRequest
import com.wastesamaritan.task_assignment_android.auth.data.dto.TokenResponse
import com.wastesamaritan.task_assignment_android.auth.data.dto.UserDto
import com.wastesamaritan.task_assignment_android.bookings.data.dto.BookingDto
import com.wastesamaritan.task_assignment_android.bookings.data.dto.CreateBookingRequest
import com.wastesamaritan.task_assignment_android.events.data.dto.CreateEventRequest
import com.wastesamaritan.task_assignment_android.events.data.dto.EventDto
import retrofit2.http.*

interface ApiService {
    
    // Auth endpoints
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): UserDto
    
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): TokenResponse
    
    @POST("auth/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): TokenResponse
    
    // Events endpoints
    @POST("events/")
    suspend fun createEvent(@Body request: CreateEventRequest): EventDto
    
    @GET("events/")
    suspend fun getEvents(
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 100
    ): List<EventDto>
    
    @GET("events/{event_id}")
    suspend fun getEvent(@Path("event_id") eventId: Int): EventDto
    
    @GET("events/top-booked")
    suspend fun getTopBookedEvents(@Query("limit") limit: Int = 3): List<EventDto>
    
    // Bookings endpoints
    @POST("bookings/")
    suspend fun createBooking(@Body request: CreateBookingRequest): BookingDto
    
    @GET("bookings/my")
    suspend fun getMyBookings(): List<BookingDto>
}