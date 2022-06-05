package com.fastcampus.kopring.userservice.service

import com.fastcampus.kopring.userservice.api.model.SignInRequest
import com.fastcampus.kopring.userservice.api.model.SignInResponse
import com.fastcampus.kopring.userservice.domain.entity.User
import com.fastcampus.kopring.userservice.domain.repository.UserRepository
import com.fastcampus.kopring.userservice.api.model.SignUpRequest
import com.fastcampus.kopring.userservice.config.JWTProperties
import com.fastcampus.kopring.userservice.exception.PasswordNotMatchedException
import com.fastcampus.kopring.userservice.exception.UserExistsException
import com.fastcampus.kopring.userservice.exception.UserNotFoundException
import com.fastcampus.kopring.userservice.utils.BCryptUtils
import com.fastcampus.kopring.userservice.utils.JWTUtils
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val jwtProperties: JWTProperties,
) {

    suspend fun signUp(signUpRequest: SignUpRequest) =
        signUpRequest.run {
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
        signInRequest.run {
            val user = userRepository.findByEmail(email) ?: throw UserNotFoundException()

            val verified = BCryptUtils.verify(signInRequest.password, user.password)
            if (!verified) {
                throw PasswordNotMatchedException()
            }

            SignInResponse(
                email = user.email,
                username = user.username,
                token = JWTUtils.createToken(user.email, jwtProperties),
                refreshToken = JWTUtils.createRefreshToken(user.email, jwtProperties),
            )
        }

    suspend fun get(id: Long) = userRepository.findById(id)


}