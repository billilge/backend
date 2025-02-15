package site.billilge.api.backend.global.security.oauth2

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import site.billilge.api.backend.domain.member.exception.MemberErrorCode
import site.billilge.api.backend.domain.member.repository.MemberRepository
import site.billilge.api.backend.global.exception.ApiException
import site.billilge.api.backend.global.security.jwt.TokenProvider
import site.billilge.api.backend.global.security.oauth2.userinfo.GoogleOAuth2UserInfo
import site.billilge.api.backend.global.security.oauth2.userinfo.OAuth2Provider
import site.billilge.api.backend.global.security.oauth2.userinfo.OAuth2UserInfo
import java.time.Duration
import java.util.regex.Pattern

@Component
class OAuth2AuthenticationSuccessHandler(
    private val tokenProvider: TokenProvider,

    private val memberRepository: MemberRepository,

    @Value("\${login.redirect.url}") private val redirectUrl: String,
) : SimpleUrlAuthenticationSuccessHandler() {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication?
    ) {
        val userAuthInfo = authentication?.principal as UserAuthInfo

        if (userAuthInfo.oAuth2UserInfo == null) return

        val oAuth2UserInfo = userAuthInfo.oAuth2UserInfo

        handleOAuth2Callback(oAuth2UserInfo, request, response)
    }

    private fun handleOAuth2Callback(
        oAuth2UserInfo: OAuth2UserInfo, request: HttpServletRequest?, response: HttpServletResponse?
    ) {
        val provider = oAuth2UserInfo.provider

        if (provider == OAuth2Provider.GOOGLE) {
            val googleUserInfo = oAuth2UserInfo as GoogleOAuth2UserInfo
            val email = googleUserInfo.email

            if (!email.isKMUEmail()) {
                redirectStrategy.sendRedirect(
                    request, response, getRedirectUrl(redirectUrl, OAuth2CallbackStatus.INVALID_EMAIL)
                )
                return
            }

            if (!memberRepository.existsByEmail(email)) {
                redirectStrategy.sendRedirect(
                    request, response, getRedirectUrl(redirectUrl, OAuth2CallbackStatus.NEW_MEMBER, email = email)
                )
                return
            }

            val member = memberRepository.findByEmail(email)
                ?: throw ApiException(MemberErrorCode.MEMBER_NOT_FOUND)
            val accessToken = tokenProvider.generateToken(member, Duration.ofDays(30))

            redirectStrategy.sendRedirect(
                request, response, getRedirectUrl(redirectUrl, OAuth2CallbackStatus.SUCCESS, accessToken = accessToken)
            )
        }
    }

    private fun getRedirectUrl(
        targetUrl: String,
        status: OAuth2CallbackStatus,
        email: String? = null,
        accessToken: String? = null,
    ): String {
        val uriBuilder =
            UriComponentsBuilder.fromUriString("${targetUrl}/callback").queryParam("status", status.toString())

        if (status == OAuth2CallbackStatus.SUCCESS) {
            uriBuilder.queryParam("accessToken", accessToken)
        } else if (status == OAuth2CallbackStatus.NEW_MEMBER) {
            uriBuilder.queryParam("email", email)
        }

        return uriBuilder.build().toUriString()
    }

    private fun String.isKMUEmail(): Boolean =
        Pattern.matches("[0-9a-zA-Z]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", this) && this.endsWith("@kookmin.ac.kr")

    enum class OAuth2CallbackStatus {
        SUCCESS, INVALID_EMAIL, NEW_MEMBER
    }
}