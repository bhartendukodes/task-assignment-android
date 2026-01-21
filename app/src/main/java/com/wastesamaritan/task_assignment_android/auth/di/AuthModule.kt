package com.wastesamaritan.task_assignment_android.auth.di

import com.wastesamaritan.task_assignment_android.auth.data.repository.AuthRepositoryImpl
import com.wastesamaritan.task_assignment_android.auth.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}