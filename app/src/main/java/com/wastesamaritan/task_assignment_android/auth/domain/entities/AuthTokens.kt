package com.wastesamaritan.task_assignment_android.auth.domain.entities

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String
)