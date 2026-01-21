package com.wastesamaritan.task_assignment_android.core.network

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException

@Serializable
data class ErrorResponse(
    val detail: String? = null,
    val message: String? = null
)

suspend fun <T> safeApiCall(apiCall: suspend () -> T): ApiResult<T> {
    return try {
        ApiResult.Success(apiCall())
    } catch (throwable: Throwable) {
        when (throwable) {
            is IOException -> {
                ApiResult.Error(NetworkException.NetworkError("Network error occurred. Please check your internet connection."))
            }
            is HttpException -> {
                val errorMessage = extractErrorMessage(throwable)
                when (throwable.code()) {
                    400 -> ApiResult.Error(NetworkException.BadRequest(errorMessage))
                    401 -> ApiResult.Error(NetworkException.Unauthorized(errorMessage))
                    403 -> ApiResult.Error(NetworkException.Forbidden(errorMessage))
                    404 -> ApiResult.Error(NetworkException.NotFound(errorMessage))
                    409 -> ApiResult.Error(NetworkException.Conflict(errorMessage))
                    422 -> ApiResult.Error(NetworkException.UnprocessableEntity(errorMessage))
                    429 -> ApiResult.Error(NetworkException.TooManyRequests(errorMessage))
                    500 -> ApiResult.Error(NetworkException.InternalServerError(errorMessage))
                    else -> ApiResult.Error(NetworkException.UnknownError(errorMessage))
                }
            }
            else -> {
                ApiResult.Error(NetworkException.UnknownError(throwable.message ?: "Unknown error occurred"))
            }
        }
    }
}

private fun extractErrorMessage(httpException: HttpException): String {
    return try {
        val errorBody = httpException.response()?.errorBody()?.string()
        if (!errorBody.isNullOrBlank()) {
            try {
                val json = Json { ignoreUnknownKeys = true; isLenient = true }
                val errorResponse = json.decodeFromString<ErrorResponse>(errorBody)
                errorResponse.detail ?: errorResponse.message ?: getDefaultErrorMessage(httpException.code())
            } catch (e: Exception) {
                // If JSON parsing fails, try to extract message from error body using regex
                val detailMatch = Regex("\"detail\"\\s*:\\s*\"([^\"]+)\"").find(errorBody)
                detailMatch?.groupValues?.get(1)?.takeIf { it.isNotBlank() }
                    ?: Regex("\"message\"\\s*:\\s*\"([^\"]+)\"").find(errorBody)?.groupValues?.get(1)?.takeIf { it.isNotBlank() }
                    ?: errorBody.takeIf { it.length < 200 } // Use raw body if it's short and readable
                    ?: getDefaultErrorMessage(httpException.code())
            }
        } else {
            getDefaultErrorMessage(httpException.code())
        }
    } catch (e: Exception) {
        getDefaultErrorMessage(httpException.code())
    }
}

private fun getDefaultErrorMessage(code: Int): String {
    return when (code) {
        400 -> "Bad request. Please check your input."
        401 -> "Authentication failed. Please login again."
        403 -> "Access forbidden. You don't have permission."
        404 -> "Resource not found."
        409 -> "Resource conflict. This resource already exists."
        422 -> "Validation failed. Please check your input."
        429 -> "Rate limit exceeded. Please try again later."
        500 -> "Server error. Please try again later."
        else -> "An error occurred. Please try again."
    }
}

sealed class NetworkException(message: String) : Exception(message) {
    class NetworkError(message: String) : NetworkException(message)
    class BadRequest(message: String) : NetworkException(message)
    class Unauthorized(message: String) : NetworkException(message)
    class Forbidden(message: String) : NetworkException(message)
    class NotFound(message: String) : NetworkException(message)
    class Conflict(message: String) : NetworkException(message)
    class UnprocessableEntity(message: String) : NetworkException(message)
    class TooManyRequests(message: String) : NetworkException(message)
    class InternalServerError(message: String) : NetworkException(message)
    class UnknownError(message: String) : NetworkException(message)
}