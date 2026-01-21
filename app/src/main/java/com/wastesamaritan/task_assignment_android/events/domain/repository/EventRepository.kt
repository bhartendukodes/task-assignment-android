package com.wastesamaritan.task_assignment_android.events.domain.repository

import com.wastesamaritan.task_assignment_android.core.network.ApiResult
import com.wastesamaritan.task_assignment_android.events.domain.entities.Event

interface EventRepository {
    suspend fun createEvent(
        title: String,
        description: String?,
        location: String,
        eventDate: String,
        totalSlots: Int,
        imageUrl: String?
    ): ApiResult<Event>
    suspend fun getEvents(skip: Int = 0, limit: Int = 100): ApiResult<List<Event>>
    suspend fun getEvent(eventId: Int): ApiResult<Event>
    suspend fun getTopBookedEvents(limit: Int = 3): ApiResult<List<Event>>
}