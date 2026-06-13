package site.billilge.api.backend.common.exception

import org.springframework.http.HttpStatus

interface ErrorCode {
    val name: String
    val message: String
    val httpStatus: HttpStatus
}
