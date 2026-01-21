package com.wastesamaritan.task_assignment_android.events.domain.entities

data class Event(
    val id: Int,
    val title: String,
    val description: String?,
    val location: String,
    val eventDate: String,
    val totalSlots: Int,
    val imageUrl: String?,
    val createdAt: String,
    val availableSlots: Int?,
    val bookingCount: Int?
)