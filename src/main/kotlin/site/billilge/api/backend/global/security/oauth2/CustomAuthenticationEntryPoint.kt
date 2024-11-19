package site.billilge.api.backend.global.security.oauth2

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import site.billilge.api.backend.global.exception.ErrorResponseDTO
import java.time.Instant

//안쓰는 클래스 22
class CustomAuthenticationEntryPoint: AuthenticationEntryPoint {
    override fun commence(request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException) {
        returnErrorResponse(response, authException)
    }

    private fun returnErrorResponse(response: HttpServletResponse, exception: Exception) {
        val objectMapper = ObjectMapper()
        response.status = HttpServletResponse.SC_UNAUTHORIZED // 401 Unauthorized
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        val error: ErrorResponseDTO = ErrorResponseDTO.create(exception, Instant.now())
        response.writer.write(objectMapper.writeValueAsString(error))
    }
}