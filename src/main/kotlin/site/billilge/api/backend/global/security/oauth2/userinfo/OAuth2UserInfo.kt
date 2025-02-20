package site.billilge.api.backend.global.security.oauth2.userinfo

interface OAuth2UserInfo {
    val provider: OAuth2Provider
    val attributes: Map<String, Any?>
}