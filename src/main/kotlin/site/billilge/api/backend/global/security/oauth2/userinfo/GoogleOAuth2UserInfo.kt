package site.billilge.api.backend.global.security.oauth2.userinfo

data class GoogleOAuth2UserInfo(
    override val attributes: Map<String, Any?>
): OAuth2UserInfo {
    override val provider = OAuth2Provider.GOOGLE
    val email: String
        get() = attributes["email"] as? String ?: ""
}