package com.wastesamaritan.task_assignment_android.events.domain.usecases

import com.wastesamaritan.task_assignment_android.core.network.ApiResult
import com.wastesamaritan.task_assignment_android.events.domain.entities.Event
import com.wastesamaritan.task_assignment_android.events.domain.repository.EventRepository
import javax.inject.Inject

class CreateEventUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(
        title: String,
        description: String?,
        location: String,
        eventDate: String,
        totalSlots: Int,
        imageUrl: String?
    ): ApiResult<Event> {
        return when {
            title.isBlank() -> ApiResult.Error(IllegalArgumentException("Title cannot be empty"))
            location.isBlank() -> ApiResult.Error(IllegalArgumentException("Location cannot be empty"))
            totalSlots <= 0 -> ApiResult.Error(IllegalArgumentException("Total slots must be greater than 0"))
            eventDate.isBlank() -> ApiResult.Error(IllegalArgumentException("Event date cannot be empty"))
            else -> eventRepository.createEvent(
                title = title.trim(),
                description = description?.trim(),
                location = location.trim(),
                eventDate = eventDate,
                totalSlots = totalSlots,
                imageUrl = imageUrl?.trim()
            )
        }
    }
}