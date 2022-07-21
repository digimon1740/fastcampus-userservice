package com.fastcampus.kopring.userservice.web

import com.fastcampus.kopring.userservice.model.*
import com.fastcampus.kopring.userservice.service.UserService
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
import java.nio.file.Paths

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
) {

    val path by lazy { Paths.get("./src/main/resources/images/") }

    // JWT 인증
    @PostMapping("/signup")
    suspend fun signUp(@RequestBody signUpRequest: SignUpRequest) {
        userService.signUp(signUpRequest)
    }

    // JWT 인증
    @PostMapping("/signin")
    suspend fun signIn(@RequestBody signInRequest: SignInRequest): SignInResponse {
        return userService.signIn(signInRequest)
    }

    @GetMapping("/me")
    suspend fun get(
        @AuthToken token: String,
    ): MeResponse {
        return MeResponse(userService.getByToken(token))
    }

    @DeleteMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun logout(@AuthToken token: String) {
        userService.logout(token)
    }

    @GetMapping("/{userId}/username")
    suspend fun getUsername(@PathVariable userId: Long): Map<String, String> {
        return mapOf("reporter" to userService.get(userId).username)
    }

    @PostMapping("/{id}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    suspend fun edit(
        @PathVariable id: Long,
        @ModelAttribute request: UserEditRequest,
        @AuthToken token: String,
        @RequestPart("profileUrl") filePart: FilePart
    ) {
        val orgFilename = filePart.filename()
        var filename: String? = null
        if (orgFilename.isNotEmpty()) {
            val ext = orgFilename.substring(orgFilename.lastIndexOf(".") + 1)
            filename = "${id}.${ext}"
            filePart.transferTo(path.resolve(filename)).awaitSingleOrNull()
        }

        userService.edit(token, request.username, filename)
    }

}