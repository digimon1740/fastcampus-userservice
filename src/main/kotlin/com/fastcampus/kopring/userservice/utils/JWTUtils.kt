package com.fastcampus.kopring.userservice.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm.HMAC256
import com.auth0.jwt.interfaces.DecodedJWT
import com.fastcampus.kopring.userservice.config.JWTProperties
import java.util.*

// JWT 인증
object JWTUtils {

    fun createToken(claim: JWTClaim, properties: JWTProperties) =
        JWT.create()
            .withIssuer(properties.issuer)
            .withSubject(properties.subject)
            .withIssuedAt(Date())
            .withExpiresAt(Date(Date().time + properties.expiresTime * 1000))
            .withClaim("userId", claim.userId)
            .withClaim("profileUrl", claim.profileUrl)
            .withClaim("username", claim.username)
            .withClaim("email", claim.email)
            .sign(HMAC256(properties.secret))


    fun createRefreshToken(claim: JWTClaim, properties: JWTProperties) =
        JWT.create()
            .withIssuer(properties.issuer)
            .withSubject(properties.subject)
            .withIssuedAt(Date())
            .withExpiresAt(Date(Date().time + properties.refreshExpiresTime * 1000))
            .withClaim("userId", claim.userId)
            .withClaim("profileUrl", claim.profileUrl)
            .withClaim("username", claim.username)
            .withClaim("email", claim.email)
            .sign(HMAC256(properties.refreshSecret))


    fun decode(token: String, secret: String, issuer: String): DecodedJWT {
        val algorithm = HMAC256(secret)

        val verifier = JWT.require(algorithm)
            .withIssuer(issuer)
            .build()

        return verifier.verify(token)
    }

}

data class JWTClaim(
    val userId: Long,
    val email: String,
    val profileUrl: String?,
    val username: String,
)


