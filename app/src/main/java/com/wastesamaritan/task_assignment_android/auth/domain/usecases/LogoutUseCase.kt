package com.wastesamaritan.task_assignment_android.auth.domain.usecases

import com.wastesamaritan.task_assignment_android.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        authRepository.logout()
    }
}