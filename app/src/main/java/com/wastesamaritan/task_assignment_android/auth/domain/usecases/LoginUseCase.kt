package com.wastesamaritan.task_assignment_android.auth.domain.usecases

import com.wastesamaritan.task_assignment_android.auth.domain.entities.AuthTokens
import com.wastesamaritan.task_assignment_android.auth.domain.repository.AuthRepository
import com.wastesamaritan.task_assignment_android.core.network.ApiResult
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): ApiResult<AuthTokens> {
        return when {
            email.isBlank() -> ApiResult.Error(IllegalArgumentException("Email cannot be empty"))
            password.isBlank() -> ApiResult.Error(IllegalArgumentException("Password cannot be empty"))
            !isValidEmail(email) -> ApiResult.Error(IllegalArgumentException("Invalid email format"))
            else -> authRepository.login(email, password)
        }
    }
    
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}