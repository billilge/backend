package site.billilge.api.backend.global.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import site.billilge.api.backend.global.exception.ApiException
import site.billilge.api.backend.global.exception.ErrorResponse
import site.billilge.api.backend.global.exception.GlobalErrorCode
import java.io.IOException
import java.time.LocalDateTime
import java.util.*


class TokenAuthenticationFilter(
    private val tokenProvider: TokenProvider
): OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val authorizationHeader = request.getHeader(HEADER_AUTHORIZATION)

        authorizationHeader?.let {
            val accessToken = getAccessToken(authorizationHeader);

            kotlin.runCatching {
                if (tokenProvider.validToken(accessToken)) {
                    SecurityContextHolder.getContext().authentication = tokenProvider.getAuthentication(accessToken)
                }
            }.onFailure { exception ->
                if (exception !is ApiException) return

                handleException(response, exception)
            }
        }

        filterChain.doFilter(request, response)
    }

    @Throws(IOException::class)
    private fun handleException(response: HttpServletResponse, exception: ApiException) {
        val errorResponse = ErrorResponse.from(exception, LocalDateTime.now())

        val content = ObjectMapper().writeValueAsString(errorResponse)

        response.addHeader("Content-Type", "application/json")
        response.writer.write(content)
        response.writer.flush()
    }


    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val excludes = arrayOf("/auth/")
        val path = request.requestURI
        return excludes.any { path.startsWith(it) }
    }

    private fun getAccessToken(authorizationHeader: String): String {
        if (authorizationHeader.startsWith(BEARER_AUTH)) {
            return authorizationHeader.substring(BEARER_AUTH.length)
        }

        throw ApiException(GlobalErrorCode.INVALID_AUTHORIZATION_HEADER)
    }

    companion object {
        const val HEADER_AUTHORIZATION: String = "Authorization"
        const val BEARER_AUTH: String = "Bearer "
    }
}