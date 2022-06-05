package com.fastcampus.kopring.userservice.domain.repository

import com.fastcampus.kopring.userservice.domain.entity.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserRepository : CoroutineCrudRepository<User, Long> {

    suspend fun findByEmail(email: String): User?
}