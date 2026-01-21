package com.wastesamaritan.task_assignment_android.bookings.data.dto

import com.wastesamaritan.task_assignment_android.auth.data.dto.UserDto
import com.wastesamaritan.task_assignment_android.events.data.dto.EventDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookingDto(
    @SerialName("id")
    val id: Int,
    @SerialName("user_id")
    val userId: Int,
    @SerialName("event_id")
    val eventId: Int,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("user")
    val user: UserDto? = null,
    @SerialName("event")
    val event: EventDto? = null
)