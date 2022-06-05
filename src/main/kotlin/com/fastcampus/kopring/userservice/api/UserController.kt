package com.fastcampus.kopring.userservice.api

import com.fastcampus.kopring.userservice.api.model.SignInRequest
import com.fastcampus.kopring.userservice.api.model.SignUpRequest
import com.fastcampus.kopring.userservice.api.model.UserEditRequest
import com.fastcampus.kopring.userservice.service.UserService
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
import java.io.File

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
) {

    // JWT 인증
    @PostMapping("/signup")
    suspend fun signUp(@RequestBody signUpRequest: SignUpRequest) = userService.signUp(signUpRequest)

    // JWT 인증
    @PostMapping("/signin")
    suspend fun signIn(@RequestBody signInRequest: SignInRequest) = userService.signIn(signInRequest)


    @GetMapping("/{id}")
    suspend fun get(@PathVariable id: Long) = userService.get(id)


    @PutMapping("/{id}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    suspend fun edit(
        @PathVariable id : Long,
        @ModelAttribute request: UserEditRequest,
        @RequestPart("file") filePart: FilePart
    ) {

        val filename = "${id}-${filePart.filename()}"
        val filepath = ""
        filePart.transferTo(File("")).awaitSingleOrNull()
    }


}