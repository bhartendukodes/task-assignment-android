package com.wastesamaritan.task_assignment_android.events.data.repository

import com.wastesamaritan.task_assignment_android.core.network.ApiResult
import com.wastesamaritan.task_assignment_android.core.network.ApiService
import com.wastesamaritan.task_assignment_android.core.network.safeApiCall
import com.wastesamaritan.task_assignment_android.events.data.dto.CreateEventRequest
import com.wastesamaritan.task_assignment_android.events.data.mappers.toDomain
import com.wastesamaritan.task_assignment_android.events.domain.entities.Event
import com.wastesamaritan.task_assignment_android.events.domain.repository.EventRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : EventRepository {
    
    override suspend fun createEvent(
        title: String,
        description: String?,
        location: String,
        eventDate: String,
        totalSlots: Int,
        imageUrl: String?
    ): ApiResult<Event> {
        return safeApiCall {
            val request = CreateEventRequest(
                title = title,
                description = description,
                location = location,
                eventDate = eventDate,
                totalSlots = totalSlots,
                imageUrl = imageUrl
            )
            apiService.createEvent(request).toDomain()
        }
    }
    
    override suspend fun getEvents(skip: Int, limit: Int): ApiResult<List<Event>> {
        return safeApiCall {
            apiService.getEvents(skip, limit).map { it.toDomain() }
        }
    }
    
    override suspend fun getEvent(eventId: Int): ApiResult<Event> {
        return safeApiCall {
            apiService.getEvent(eventId).toDomain()
        }
    }
    
    override suspend fun getTopBookedEvents(limit: Int): ApiResult<List<Event>> {
        return safeApiCall {
            apiService.getTopBookedEvents(limit).map { it.toDomain() }
        }
    }
}