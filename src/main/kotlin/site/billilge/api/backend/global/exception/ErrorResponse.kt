package site.billilge.api.backend.global.exception

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

@Schema
data class ErrorResponse(
    @field:Schema(description = "에러 코드", example = "INTERNAL_SERVER_ERROR")
    val code: String,
    @field:Schema(description = "오류 메시지", example = "오류 메시지입니다.")
    val message: String,
    @field:Schema(description = "HTTP 응답 코드", example = "500")
    val status: Int,
    @field:Schema(description = "발생 시각")
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