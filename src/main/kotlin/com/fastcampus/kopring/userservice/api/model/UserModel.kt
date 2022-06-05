package com.fastcampus.kopring.userservice.api.model

import com.fastcampus.kopring.userservice.domain.entity.User
import java.time.LocalDateTime

data class UserResponse(
    val email: String,
    val username: String,
    val createdAt: LocalDateTime?,
) {

    companion object {
        operator fun invoke(user: User) =
            UserResponse(
                email = user.email,
                username = user.username,
                createdAt = user.createdAt,
            )
    }
}

data class UserEditRequest(
    val username: String,
)