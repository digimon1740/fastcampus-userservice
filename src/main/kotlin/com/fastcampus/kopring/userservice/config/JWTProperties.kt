package com.fastcampus.kopring.userservice.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

// JWT 인증
@ConstructorBinding
@ConfigurationProperties(prefix = "jwt")
data class JWTProperties(
    val issuer: String,
    val subject: String,
    val expiresTime: Long,
    val refreshExpiresTime: Long,
    val secret: String,
    val refreshSecret: String,
)
