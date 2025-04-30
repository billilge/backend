package site.billilge.api.backend.global.exception

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema
data class ErrorResponse(
    @field:Schema(description = "에러 코드", example = "INTERNAL_SERVER_ERROR")
    val code: String,
    @field:Schema(description = "오류 메시지", example = "오류 메시지입니다.")
    val message: String,
    @field:Schema(description = "HTTP 응답 코드", example = "500")
    val status: Int,
    @field:Schema(description = "발생 시각")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val timestamp: LocalDateTime
) {
    companion object {
        @JvmStatic
        fun from(errorCode: ErrorCode, now: LocalDateTime): ErrorResponse {
            return ErrorResponse(
                code = errorCode.name,
                message = errorCode.message,
                status = errorCode.httpStatus.value(),
                timestamp = now
            )
        }

        @JvmStatic
        fun from(exception: Exception, now: LocalDateTime): ErrorResponse {
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