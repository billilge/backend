package site.billilge.api.backend.global.logging

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

val log = KotlinLogging.logger {}

@Component
class LoggingFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val startTime = System.currentTimeMillis()

        filterChain.doFilter(request, response)

        val endTime = System.currentTimeMillis()
        val elapsedTime = endTime - startTime

        log.info { "[${request.method}] ${request.requestURI} (ip=${getIp(request)}) (status=${response.status}) ${elapsedTime}ms" }
    }

    private fun getIp(request: HttpServletRequest): String {
        val xfHeader = request.getHeader("X-Forwarded-For")

        return if (xfHeader == null) request.remoteAddr else xfHeader.split(",")[0]
    }
}