package site.billilge.api.backend.global.security.oauth2.userinfo

enum class OAuth2Provider(
    val registrationId: String
) {
    GOOGLE("google"), KOOKMIN("kookmin");

    companion object {
        @JvmStatic
        fun byRegistrationId(registrationId: String): OAuth2Provider {
            return enumValueOf(registrationId.uppercase())
        }
    }
}