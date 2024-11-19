package site.billilge.api.backend.global.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler
    fun handleApiException(exception: ApiException): ResponseEntity<ErrorResponseDTO> {
        val errorCode = exception.errorCode
        val errorResponseDTO = ErrorResponseDTO.create(errorCode, Instant.now())
        val httpStatus = errorCode.httpStatus

        return ResponseEntity(errorResponseDTO, httpStatus)
    }

    @ExceptionHandler
    fun handleDefaultException(exception: Exception): ResponseEntity<ErrorResponseDTO> {
        val errorResponseDTO = ErrorResponseDTO.create(exception, Instant.now())

        exception.printStackTrace()

        return ResponseEntity(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}