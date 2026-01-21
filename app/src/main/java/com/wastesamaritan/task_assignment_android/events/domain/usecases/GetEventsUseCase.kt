package com.wastesamaritan.task_assignment_android.events.domain.usecases

import com.wastesamaritan.task_assignment_android.core.network.ApiResult
import com.wastesamaritan.task_assignment_android.events.domain.entities.Event
import com.wastesamaritan.task_assignment_android.events.domain.repository.EventRepository
import javax.inject.Inject

class GetEventsUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(skip: Int = 0, limit: Int = 100): ApiResult<List<Event>> {
        return eventRepository.getEvents(skip, limit)
    }
}