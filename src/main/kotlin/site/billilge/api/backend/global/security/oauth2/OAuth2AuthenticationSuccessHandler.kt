package site.billilge.api.backend.global.security.oauth2

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import site.billilge.api.backend.domain.member.service.MemberService
import site.billilge.api.backend.global.security.jwt.TokenProvider
import java.time.Duration
import java.util.regex.Pattern

@Component
class OAuth2AuthenticationSuccessHandler(
    private val tokenProvider: TokenProvider,

    private val memberService: MemberService,

    @Value("\${login.redirect.url}")
    private val redirectUrl: String
): SimpleUrlAuthenticationSuccessHandler() {
    override fun onAuthenticationSuccess(request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication?) {
        val userAuthInfo = authentication?.principal as UserAuthInfo
        val email = userAuthInfo.email

        if (!email.isKMUEmail()) {
            redirectStrategy.sendRedirect(request, response, getRedirectUrl(redirectUrl, OAuth2CallbackStatus.INVALID_EMAIL))
            return
        }

        val member = memberService.getOrCreateMember(email)

        val isNewUser = member.name.isNullOrEmpty()

        val accessToken = tokenProvider.generateToken(member, Duration.ofDays(30))

        redirectStrategy.sendRedirect(request, response, getRedirectUrl(redirectUrl, OAuth2CallbackStatus.SUCCESS, accessToken, isNewUser))
    }

    private fun getRedirectUrl(targetUrl: String, status: OAuth2CallbackStatus, accessToken: String? = null, isNewUser: Boolean = false): String {
        val uriBuilder = UriComponentsBuilder
            .fromUriString("${targetUrl}/callback")
            .queryParam("status", status.toString())

        if (status == OAuth2CallbackStatus.SUCCESS) {
            uriBuilder
                .queryParam("accessToken", accessToken)
                .queryParam("isNewUser", isNewUser)
        }

        return uriBuilder
            .build()
            .toUriString()
    }

    private fun String.isKMUEmail(): Boolean =
        Pattern.matches("[0-9a-zA-Z]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", this) && this.endsWith("@kookmin.ac.kr")

    enum class OAuth2CallbackStatus {
        SUCCESS, INVALID_EMAIL
    }
}