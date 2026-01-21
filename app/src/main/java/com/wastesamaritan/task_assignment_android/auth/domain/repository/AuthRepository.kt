package com.wastesamaritan.task_assignment_android.auth.domain.repository

import com.wastesamaritan.task_assignment_android.auth.domain.entities.AuthTokens
import com.wastesamaritan.task_assignment_android.auth.domain.entities.User
import com.wastesamaritan.task_assignment_android.core.network.ApiResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): ApiResult<AuthTokens>
    suspend fun register(name: String, email: String, password: String): ApiResult<User>
    suspend fun refreshToken(refreshToken: String): ApiResult<AuthTokens>
    suspend fun logout()
    fun isLoggedIn(): Flow<Boolean>
    suspend fun isLoggedInSync(): Boolean
}