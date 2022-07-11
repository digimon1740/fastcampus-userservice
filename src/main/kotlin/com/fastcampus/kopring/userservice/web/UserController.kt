package com.fastcampus.kopring.userservice.web

import com.fastcampus.kopring.userservice.model.MeResponse
import com.fastcampus.kopring.userservice.model.SignInRequest
import com.fastcampus.kopring.userservice.model.SignUpRequest
import com.fastcampus.kopring.userservice.model.UserEditRequest
import com.fastcampus.kopring.userservice.service.UserService
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.http.HttpStatus
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

    @GetMapping("/me")
    suspend fun get(@RequestHeader("Authorization") authHeader: String): MeResponse {
        val token = authHeader.split(" ")[1]
        return MeResponse(userService.getByToken(token))
    }

    @DeleteMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun logout(@RequestHeader("Authorization") authHeader: String) {
        val token = authHeader.split(" ")[1]
        userService.logout(token)
    }

    @GetMapping("/{userId}/username")
    suspend fun getUsername(@PathVariable userId: Long) = mapOf("reporter" to userService.get(userId).username)

    @PostMapping("/{id}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    suspend fun edit(
        @PathVariable id: Long,
        @ModelAttribute request: UserEditRequest,
        @RequestPart("profileUrl") filePart: FilePart
    ) {

        val filename = "${id}-${filePart.filename()}"
        val filepath = ""
        filePart.transferTo(File("/Users/sanghoon/IdeaProjects/fastcampus-userservice/src/main/resources/${filePart.filename()}"))
            .awaitSingleOrNull()
    }


}