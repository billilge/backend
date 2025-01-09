package site.billilge.api.backend.global.exception

import java.time.Instant

data class ErrorResponse(
    val code: String,
    val message: String,
    val status: Int,
    val timestamp: Instant
) {
    companion object {
        @JvmStatic
        fun from(errorCode: ErrorCode, now: Instant): ErrorResponse {
            return ErrorResponse(
                code = errorCode.name,
                message = errorCode.message,
                status = errorCode.httpStatus.value(),
                timestamp = now
            )
        }

        @JvmStatic
        fun from(exception: Exception, now: Instant): ErrorResponse {
            val errorCode = GlobalErrorCode.INTERNAL_SERVER_ERROR

            return ErrorResponse(
                code = errorCode.name,
                message = exception.message ?: errorCode.message,
                status = errorCode.httpStatus.value(),
                timestamp = now
            )
        }
    }
}