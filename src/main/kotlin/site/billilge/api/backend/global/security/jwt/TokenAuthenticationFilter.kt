package site.billilge.api.backend.global.security.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import site.billilge.api.backend.global.exception.ApiException
import site.billilge.api.backend.global.exception.GlobalErrorCode

class TokenAuthenticationFilter(
    private val tokenProvider: TokenProvider
): OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val authorizationHeader = request.getHeader(HEADER_AUTHORIZATION)

        authorizationHeader?.let {
            val accessToken = getAccessToken(authorizationHeader);

            if (tokenProvider.validToken(accessToken)) {
                SecurityContextHolder.getContext().authentication = tokenProvider.getAuthentication(accessToken)
            }
        }

        filterChain.doFilter(request, response)
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