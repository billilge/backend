package site.billilge.api.backend.global.security.oauth2.userinfo

import site.billilge.api.backend.global.exception.GlobalErrorCode
import site.billilge.api.backend.global.exception.ApiException

object OAuth2UserInfoFactory {
    fun createOAuth2UserInfo(registrationId: String, attributes: Map<String, Any>): OAuth2UserInfo {
        return when(OAuth2Provider.byRegistrationId(registrationId)) {
            OAuth2Provider.GOOGLE -> GoogleOAuth2UserInfo(attributes)
            else -> throw ApiException(GlobalErrorCode.INVALID_OAUTH2_PROVIDER)
        }
    }
}