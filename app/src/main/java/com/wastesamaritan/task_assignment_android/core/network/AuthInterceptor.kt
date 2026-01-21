package com.wastesamaritan.task_assignment_android.core.network

import com.wastesamaritan.task_assignment_android.auth.data.dto.RefreshTokenRequest
import com.wastesamaritan.task_assignment_android.core.datastore.TokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // Skip auth for login, register, and refresh endpoints
        if (shouldSkipAuth(originalRequest.url.encodedPath)) {
            return chain.proceed(originalRequest)
        }
        
        val accessToken = runBlocking { tokenManager.getAccessToken().first() }
        
        val requestWithAuth = if (accessToken != null) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
        } else {
            originalRequest
        }
        
        val response = chain.proceed(requestWithAuth)
        
        // Handle 401 - token expired
        if (response.code == 401 && accessToken != null) {
            response.close()
            
            val refreshToken = runBlocking { tokenManager.getRefreshToken().first() }
            if (refreshToken != null) {
                return handleTokenRefresh(chain, originalRequest, refreshToken)
            }
        }
        
        return response
    }
    
    private fun shouldSkipAuth(path: String): Boolean {
        return path.contains("auth/login") || 
               path.contains("auth/register") || 
               path.contains("auth/refresh")
    }
    
    private fun handleTokenRefresh(
        chain: Interceptor.Chain,
        originalRequest: Request,
        refreshToken: String
    ): Response {
        return try {
            // For now, just clear tokens and proceed with original request
            // In a full implementation, you'd create a separate OkHttp client 
            // without this interceptor to avoid recursion
            runBlocking { tokenManager.clearTokens() }
            chain.proceed(originalRequest)
        } catch (e: Exception) {
            // Refresh failed, clear tokens
            runBlocking { tokenManager.clearTokens() }
            chain.proceed(originalRequest)
        }
    }
}