package site.billilge.api.backend.global.exception

import org.springframework.http.HttpStatus

interface ErrorCode {
    val name: String
    val message: String
    val httpStatus: HttpStatus
}