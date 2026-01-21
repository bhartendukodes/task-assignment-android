package com.wastesamaritan.task_assignment_android.auth.data.mappers

import com.wastesamaritan.task_assignment_android.auth.data.dto.TokenResponse
import com.wastesamaritan.task_assignment_android.auth.data.dto.UserDto
import com.wastesamaritan.task_assignment_android.auth.domain.entities.AuthTokens
import com.wastesamaritan.task_assignment_android.auth.domain.entities.User

fun UserDto.toDomain(): User {
    return User(
        id = id,
        name = name,
        email = email,
        createdAt = createdAt
    )
}

fun TokenResponse.toDomain(): AuthTokens {
    return AuthTokens(
        accessToken = accessToken,
        refreshToken = refreshToken,
        tokenType = tokenType
    )
}