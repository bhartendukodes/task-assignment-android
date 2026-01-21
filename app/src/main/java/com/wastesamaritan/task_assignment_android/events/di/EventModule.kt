package com.wastesamaritan.task_assignment_android.events.di

import com.wastesamaritan.task_assignment_android.events.data.repository.EventRepositoryImpl
import com.wastesamaritan.task_assignment_android.events.domain.repository.EventRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class EventModule {
    
    @Binds
    @Singleton
    abstract fun bindEventRepository(
        eventRepositoryImpl: EventRepositoryImpl
    ): EventRepository
}