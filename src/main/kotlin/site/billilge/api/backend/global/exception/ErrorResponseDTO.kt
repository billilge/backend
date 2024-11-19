package site.billilge.api.backend.global.exception

import java.time.Instant

data class ErrorResponseDTO(
    val code: String,
    val message: String,
    val status: Int,
    val timestamp: Instant
) {
    companion object {
        @JvmStatic
        fun create(errorCode: ErrorCode, now: Instant): ErrorResponseDTO {
            return ErrorResponseDTO(
                code = errorCode.name,
                message = errorCode.message,
                status = errorCode.httpStatus.value(),
                timestamp = now
            )
        }

        @JvmStatic
        fun create(exception: Exception, now: Instant): ErrorResponseDTO {
            val errorCode = GlobalErrorCode.INTERNAL_SERVER_ERROR

            return ErrorResponseDTO(
                code = errorCode.name,
                message = exception.message ?: errorCode.message,
                status = errorCode.httpStatus.value(),
                timestamp = now
            )
        }
    }
}