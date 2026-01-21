package com.wastesamaritan.task_assignment_android.auth.domain.usecases

import com.wastesamaritan.task_assignment_android.auth.domain.entities.User
import com.wastesamaritan.task_assignment_android.auth.domain.repository.AuthRepository
import com.wastesamaritan.task_assignment_android.core.network.ApiResult
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String): ApiResult<User> {
        return when {
            name.isBlank() -> ApiResult.Error(IllegalArgumentException("Name cannot be empty"))
            email.isBlank() -> ApiResult.Error(IllegalArgumentException("Email cannot be empty"))
            password.isBlank() -> ApiResult.Error(IllegalArgumentException("Password cannot be empty"))
            password.length < 6 -> ApiResult.Error(IllegalArgumentException("Password must be at least 6 characters"))
            !isValidEmail(email) -> ApiResult.Error(IllegalArgumentException("Invalid email format"))
            else -> authRepository.register(name, email, password)
        }
    }
    
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}