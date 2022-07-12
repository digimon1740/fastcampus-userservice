package com.fastcampus.kopring.userservice.service

import com.fastcampus.kopring.userservice.config.JWTProperties
import com.fastcampus.kopring.userservice.domain.entity.User
import com.fastcampus.kopring.userservice.domain.repository.UserRepository
import com.fastcampus.kopring.userservice.exception.InvalidJwtTokenException
import com.fastcampus.kopring.userservice.exception.PasswordNotMatchedException
import com.fastcampus.kopring.userservice.exception.UserExistsException
import com.fastcampus.kopring.userservice.exception.UserNotFoundException
import com.fastcampus.kopring.userservice.model.SignInRequest
import com.fastcampus.kopring.userservice.model.SignInResponse
import com.fastcampus.kopring.userservice.model.SignUpRequest
import com.fastcampus.kopring.userservice.model.UserEditRequest
import com.fastcampus.kopring.userservice.utils.BCryptUtils
import com.fastcampus.kopring.userservice.utils.JWTClaim
import com.fastcampus.kopring.userservice.utils.JWTUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val jwtProperties: JWTProperties,
    private val coroutineCacheManager: CoroutineCacheManager<User>,

    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.issuer}") private val issuer: String,
) {

    suspend fun signUp(signUpRequest: SignUpRequest) =
        with(signUpRequest) {
            userRepository.findByEmail(email)?.let {
                throw UserExistsException()
            }
            val user = User(
                email = email,
                password = BCryptUtils.hash(password),
                username = username,
            )
            userRepository.save(user)
        }

    suspend fun signIn(signInRequest: SignInRequest) =
        with(userRepository.findByEmail(signInRequest.email) ?: throw UserNotFoundException()) {
            val verified = BCryptUtils.verify(signInRequest.password, password)
            if (!verified) {
                throw PasswordNotMatchedException()
            }
            val jwtClaim = JWTClaim(
                userId = id!!,
                email = email,
                profileUrl = profileUrl,
                username = username
            )

            val token = JWTUtils.createToken(jwtClaim, jwtProperties)
            val refreshToken = JWTUtils.createRefreshToken(jwtClaim, jwtProperties)

            coroutineCacheManager.awaitPut(key = token, value = this)

            SignInResponse(
                email = email,
                username = username,
                token = token,
                refreshToken = refreshToken,
            )
        }

    suspend fun getByToken(token: String): User {
        val decodedJWT = JWTUtils.decode(token, secret, issuer)
        val cachedUser = coroutineCacheManager.awaitGetOrPut(token) {
            val userId = decodedJWT.claims["userId"]?.asLong() ?: throw InvalidJwtTokenException()
            get(userId)
        }
        return cachedUser
    }

    suspend fun get(userId: Long): User {
        return userRepository.findById(userId) ?: throw UserNotFoundException()
    }

    suspend fun logout(token: String) {
        coroutineCacheManager.awaitEvict(token)
    }

    suspend fun edit(token: String, request: UserEditRequest): User {
        val user = getByToken(token)

        return userRepository.save(user.copy(username = request.username)).also {
            coroutineCacheManager.awaitPut(key = token, value = it)
        }
    }


}