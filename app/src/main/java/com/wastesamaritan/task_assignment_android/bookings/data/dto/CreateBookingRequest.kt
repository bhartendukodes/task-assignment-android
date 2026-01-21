package com.wastesamaritan.task_assignment_android.bookings.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateBookingRequest(
    @SerialName("event_id")
    val eventId: Int
)