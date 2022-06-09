package com.fastcampus.kopring.userservice.web

import com.fastcampus.kopring.userservice.exception.InvalidJwtTokenException
import com.fastcampus.kopring.userservice.model.MeResponse
import com.fastcampus.kopring.userservice.model.SignInRequest
import com.fastcampus.kopring.userservice.model.SignUpRequest
import com.fastcampus.kopring.userservice.model.UserEditRequest
import com.fastcampus.kopring.userservice.service.UserService
import com.fastcampus.kopring.userservice.utils.JWTUtils
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
import java.io.File

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.issuer}") private val issuer: String,
) {

    // JWT 인증
    @PostMapping("/signup")
    suspend fun signUp(@RequestBody signUpRequest: SignUpRequest) = userService.signUp(signUpRequest)

    // JWT 인증
    @PostMapping("/signin")
    suspend fun signIn(@RequestBody signInRequest: SignInRequest) = userService.signIn(signInRequest)

    @GetMapping("/me")
    suspend fun get(@RequestHeader("Authorization") authHeader: String): MeResponse {
        val token = authHeader.split(" ")[1]
        val decodedJWT = JWTUtils.decode(token, secret, issuer)
        val userId = decodedJWT.claims["userId"]?.asLong() ?: throw InvalidJwtTokenException()
        return MeResponse(userService.get(userId))
    }

    @PutMapping("/{id}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    suspend fun edit(
        @PathVariable id: Long,
        @ModelAttribute request: UserEditRequest,
        @RequestPart("file") filePart: FilePart
    ) {

        val filename = "${id}-${filePart.filename()}"
        val filepath = ""
        filePart.transferTo(File("")).awaitSingleOrNull()
    }


}