package com.fastcampus.kopring.userservice.service

import com.fastcampus.kopring.userservice.config.JWTProperties
import com.fastcampus.kopring.userservice.domain.entity.User
import com.fastcampus.kopring.userservice.domain.repository.UserRepository
import com.fastcampus.kopring.userservice.exception.PasswordNotMatchedException
import com.fastcampus.kopring.userservice.exception.UserExistsException
import com.fastcampus.kopring.userservice.exception.UserNotFoundException
import com.fastcampus.kopring.userservice.model.SignInRequest
import com.fastcampus.kopring.userservice.model.SignInResponse
import com.fastcampus.kopring.userservice.model.SignUpRequest
import com.fastcampus.kopring.userservice.utils.BCryptUtils
import com.fastcampus.kopring.userservice.utils.JWTClaim
import com.fastcampus.kopring.userservice.utils.JWTUtils
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val jwtProperties: JWTProperties,
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
            SignInResponse(
                email = email,
                username = username,
                token = JWTUtils.createToken(jwtClaim, jwtProperties),
                refreshToken = JWTUtils.createRefreshToken(jwtClaim, jwtProperties),
            )
        }

    suspend fun get(id: Long): User = userRepository.findById(id) ?: throw UserNotFoundException()


}