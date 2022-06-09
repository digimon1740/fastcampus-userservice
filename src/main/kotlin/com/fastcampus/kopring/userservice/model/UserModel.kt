package com.fastcampus.kopring.userservice.model

import com.fastcampus.kopring.userservice.domain.entity.User
import java.time.LocalDateTime

data class UserEditRequest(
    val username: String,
)


data class MeResponse(
    val userId: Long,
    val profileUrl: String?,
    val username: String,
    val email: String,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
) {

    companion object {
        operator fun invoke(user: User) = with(user) {
            MeResponse(
                userId = id!!,
                profileUrl = profileUrl,
                username = username,
                email = email,
                createdAt = createdAt,
                updatedAt = updatedAt,
            )
        }
    }
}
