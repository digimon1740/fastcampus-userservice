package com.fastcampus.kopring.userservice.model

import com.fastcampus.kopring.userservice.domain.entity.User
import java.time.LocalDateTime

data class UserResponse(
    val email: String,
    val username: String,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
) {

    companion object {
        operator fun invoke(user: User) =
            UserResponse(
                email = user.email,
                username = user.username,
                createdAt = user.createdAt,
                updatedAt = user.updatedAt,
            )
    }
}

data class UserEditRequest(
    val username: String,
)