package com.wastesamaritan.task_assignment_android.events.data.mappers

import com.wastesamaritan.task_assignment_android.events.data.dto.EventDto
import com.wastesamaritan.task_assignment_android.events.domain.entities.Event

fun EventDto.toDomain(): Event {
    return Event(
        id = id,
        title = title,
        description = description,
        location = location,
        eventDate = eventDate,
        totalSlots = totalSlots,
        imageUrl = imageUrl,
        createdAt = createdAt,
        availableSlots = availableSlots,
        bookingCount = bookingCount
    )
}