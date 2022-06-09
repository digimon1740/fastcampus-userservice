package com.fastcampus.kopring.userservice.model


data class SignUpRequest(
    val email: String,
    val password: String,
    val username: String,
)

data class SignInRequest(
    val email: String,
    val password: String,
)

data class SignInResponse(
    val email: String,
    val username: String,
    val token: String,
    val refreshToken: String,
)

data class MeResponse(
    val userId: Long,
    val profileUrl: String?,
    val username: String,
    val email: String,
)
