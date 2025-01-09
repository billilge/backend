package site.billilge.api.backend.global.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ApiException::class)
    fun handleApiException(exception: ApiException): ResponseEntity<ErrorResponse> {
        val errorCode = exception.errorCode
        val errorResponse = ErrorResponse.from(errorCode, Instant.now())
        val httpStatus = errorCode.httpStatus

        return ResponseEntity(errorResponse, httpStatus)
    }

    @ExceptionHandler
    fun handleDefaultException(exception: Exception): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse.from(exception, Instant.now())

        exception.printStackTrace()

        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}