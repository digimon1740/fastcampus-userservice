package com.fastcampus.kopring.userservice.domain.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("users")
data class User(
    @Id
    var id: Long? = null,
    val email: String,
    val password: String,
    val username: String,
    @CreatedDate
    var createdAt: LocalDateTime? = null,
)