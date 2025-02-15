package site.billilge.api.backend.global.security.oauth2

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import site.billilge.api.backend.global.security.oauth2.userinfo.OAuth2UserInfoFactory

@Service
class CustomOAuth2UserService : DefaultOAuth2UserService() {
    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {
        if (userRequest == null) throw IllegalArgumentException("UserRequest cannot be null")

        val oAuth2User = super.loadUser(userRequest)
        val registrationId = userRequest.clientRegistration.registrationId

        val oAuth2UserInfo = OAuth2UserInfoFactory.createOAuth2UserInfo(registrationId, oAuth2User.attributes)

        return UserAuthInfo(oAuth2UserInfo)
    }
}