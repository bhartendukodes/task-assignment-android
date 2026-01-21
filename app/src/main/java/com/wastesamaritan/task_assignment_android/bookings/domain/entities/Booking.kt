package com.wastesamaritan.task_assignment_android.bookings.domain.entities

import com.wastesamaritan.task_assignment_android.auth.domain.entities.User
import com.wastesamaritan.task_assignment_android.events.domain.entities.Event

data class Booking(
    val id: Int,
    val userId: Int,
    val eventId: Int,
    val createdAt: String,
    val user: User?,
    val event: Event?
)