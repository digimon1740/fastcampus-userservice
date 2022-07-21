package com.fastcampus.kopring.userservice.web

import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import java.io.File
import java.io.FileInputStream
import java.nio.file.Paths

@Controller
@RequestMapping("/images")
class ImageController {

    val path by lazy { Paths.get("./src/main/resources/images/") }

    @GetMapping("{filename}")
    fun image(@PathVariable filename: String): ResponseEntity<InputStreamResource> {

        val ext = filename.substring(filename.lastIndexOf(".") + 1)
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_TYPE, "image/$ext")
            .body(InputStreamResource(FileInputStream(File("$path/$filename"))))
    }
}