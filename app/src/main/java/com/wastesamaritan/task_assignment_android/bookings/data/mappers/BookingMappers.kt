package com.wastesamaritan.task_assignment_android.bookings.data.mappers

import com.wastesamaritan.task_assignment_android.auth.data.mappers.toDomain
import com.wastesamaritan.task_assignment_android.bookings.data.dto.BookingDto
import com.wastesamaritan.task_assignment_android.bookings.domain.entities.Booking
import com.wastesamaritan.task_assignment_android.events.data.mappers.toDomain

fun BookingDto.toDomain(): Booking {
    return Booking(
        id = id,
        userId = userId,
        eventId = eventId,
        createdAt = createdAt,
        user = user?.toDomain(),
        event = event?.toDomain()
    )
}