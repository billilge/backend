package site.billilge.api.backend.global.security.oauth2

//안쓰는 클래스
class GoogleOAuth2UserInfo(private val attributes: Map<String, Any?>) {
    val name: String? = attributes["name"] as String?
    val id: String? = attributes["sub"] as String?
    val email: String? = attributes["email"] as String?
    val imageUrl: String? = attributes["picture"] as String?
}