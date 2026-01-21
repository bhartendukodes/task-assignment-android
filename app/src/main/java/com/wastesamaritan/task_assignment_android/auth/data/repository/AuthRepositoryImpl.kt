package com.wastesamaritan.task_assignment_android.auth.data.repository

import com.wastesamaritan.task_assignment_android.auth.data.dto.LoginRequest
import com.wastesamaritan.task_assignment_android.auth.data.dto.RefreshTokenRequest
import com.wastesamaritan.task_assignment_android.auth.data.dto.RegisterRequest
import com.wastesamaritan.task_assignment_android.auth.data.mappers.toDomain
import com.wastesamaritan.task_assignment_android.auth.domain.entities.AuthTokens
import com.wastesamaritan.task_assignment_android.auth.domain.entities.User
import com.wastesamaritan.task_assignment_android.auth.domain.repository.AuthRepository
import com.wastesamaritan.task_assignment_android.core.datastore.TokenManager
import com.wastesamaritan.task_assignment_android.core.network.ApiResult
import com.wastesamaritan.task_assignment_android.core.network.ApiService
import com.wastesamaritan.task_assignment_android.core.network.safeApiCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) : AuthRepository {
    
    override suspend fun login(email: String, password: String): ApiResult<AuthTokens> {
        return safeApiCall {
            val response = apiService.login(LoginRequest(email, password))
            val tokens = response.toDomain()
            tokenManager.saveTokens(tokens.accessToken, tokens.refreshToken)
            tokens
        }
    }
    
    override suspend fun register(name: String, email: String, password: String): ApiResult<User> {
        return safeApiCall {
            val response = apiService.register(RegisterRequest(name, email, password))
            response.toDomain()
        }
    }
    
    override suspend fun refreshToken(refreshToken: String): ApiResult<AuthTokens> {
        return safeApiCall {
            val response = apiService.refreshToken(RefreshTokenRequest(refreshToken))
            val tokens = response.toDomain()
            tokenManager.saveTokens(tokens.accessToken, tokens.refreshToken)
            tokens
        }
    }
    
    override suspend fun logout() {
        tokenManager.clearTokens()
    }
    
    override fun isLoggedIn(): Flow<Boolean> {
        return tokenManager.isLoggedIn()
    }
    
    override suspend fun isLoggedInSync(): Boolean {
        return tokenManager.isLoggedInSync()
    }
}