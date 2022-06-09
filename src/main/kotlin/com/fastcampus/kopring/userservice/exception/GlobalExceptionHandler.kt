package com.fastcampus.kopring.userservice.exception

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.mono
import mu.KotlinLogging
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Configuration
class GlobalExceptionHandler(private val objectMapper: ObjectMapper) : ErrorWebExceptionHandler {


    private val logger = KotlinLogging.logger {}

    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> = mono {

        logger.error(ex.stackTraceToString())

        val errorResponse = if (ex is ServerException) {
            ErrorResponse(status = ex.status, message = ex.message)
        } else {
            ErrorResponse(status = 500, message = "Internal Server Error")
        }

        with(exchange.response) {
            statusCode = HttpStatus.OK
            headers.contentType = MediaType.APPLICATION_JSON
            val dataBuffer = bufferFactory().wrap(objectMapper.writeValueAsBytes(errorResponse))
            exchange.response.writeWith(dataBuffer.toMono()).awaitFirstOrNull()
        }
    }

}