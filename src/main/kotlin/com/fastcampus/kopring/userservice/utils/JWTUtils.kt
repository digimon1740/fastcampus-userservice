package com.fastcampus.kopring.userservice.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm.*
import com.fastcampus.kopring.userservice.config.JWTProperties
import java.util.*

// JWT 인증
object JWTUtils {

    const val CLAIM = "email"

    fun createToken(email: String, properties: JWTProperties) =
        JWT.create()
            .withIssuer(properties.issuer)
            .withSubject(properties.subject)
            .withIssuedAt(Date())
            .withExpiresAt(Date(Date().time + properties.expiresTime * 1000))
            .withClaim(CLAIM, email)
            .sign(HMAC256(properties.secret))


    fun createRefreshToken(email: String, properties: JWTProperties) =
        JWT.create()
            .withIssuer(properties.issuer)
            .withSubject(properties.subject)
            .withIssuedAt(Date())
            .withExpiresAt(Date(Date().time + properties.refreshExpiresTime * 1000))
            .withClaim(CLAIM, email)
            .sign(HMAC256(properties.refreshSecret))

}