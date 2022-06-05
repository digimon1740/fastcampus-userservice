package com.fastcampus.kopring.userservice.api

import com.fastcampus.kopring.userservice.api.model.SignInRequest
import com.fastcampus.kopring.userservice.api.model.SignUpRequest
import com.fastcampus.kopring.userservice.api.model.UserResponse
import com.fastcampus.kopring.userservice.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
) {

    // JWT 인증
    @PostMapping("/signup")
    suspend fun signUp(@RequestBody signUpRequest: SignUpRequest) =
        userService.signUp(signUpRequest)

    // JWT 인증
    @PostMapping("/signin")
    suspend fun signIn(@RequestBody signInRequest: SignInRequest) =
        userService.signIn(signInRequest)


    @GetMapping("/{id}")
    suspend fun get(@PathVariable id: Long) =
        userService.get(id)?.let { UserResponse(it) }


    @PutMapping("/{id}")
    suspend fun edit() {

    }

    @PutMapping("/{id}/profile")
    suspend fun uploadProfile() {

    }

}