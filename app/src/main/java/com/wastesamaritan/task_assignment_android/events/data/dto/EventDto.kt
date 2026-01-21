package com.wastesamaritan.task_assignment_android.events.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventDto(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String? = null,
    @SerialName("location")
    val location: String,
    @SerialName("event_date")
    val eventDate: String,
    @SerialName("total_slots")
    val totalSlots: Int,
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("available_slots")
    val availableSlots: Int? = null,
    @SerialName("booking_count")
    val bookingCount: Int? = null
)