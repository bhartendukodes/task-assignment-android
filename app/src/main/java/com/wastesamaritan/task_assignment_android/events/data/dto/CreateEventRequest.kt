package com.wastesamaritan.task_assignment_android.events.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateEventRequest(
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
    val imageUrl: String? = null
)