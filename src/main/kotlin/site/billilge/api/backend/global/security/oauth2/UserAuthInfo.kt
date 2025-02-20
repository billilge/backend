package site.billilge.api.backend.global.security.oauth2

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.global.security.oauth2.userinfo.OAuth2Provider
import site.billilge.api.backend.global.security.oauth2.userinfo.OAuth2UserInfo

class UserAuthInfo: OAuth2User, UserDetails {
    private val authorities = mutableListOf<GrantedAuthority>()
    private var attributes = hashMapOf<String, Any?>()
    val oAuth2UserInfo: OAuth2UserInfo?
    val memberId: Long?

    constructor(oAuth2UserInfo: OAuth2UserInfo) {
        this.oAuth2UserInfo = oAuth2UserInfo
        memberId = null
        attributes.putAll(oAuth2UserInfo.attributes)
    }

    constructor(member: Member) {
        oAuth2UserInfo = null
        memberId = member.id
        attributes["studentId"] = member.studentId
        authorities.add(SimpleGrantedAuthority(member.role.key))
    }

    override fun getName(): String? = attributes["name"] as String?

    override fun getAttributes(): MutableMap<String, Any?> = attributes

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authorities

    override fun getPassword(): String = ""

    override fun getUsername(): String {
        oAuth2UserInfo?.let {
            if (it.provider == OAuth2Provider.GOOGLE) {
                return attributes["email"] as? String ?: ""
            }
        }

        return attributes["studentId"] as? String ?: ""
    }

    override fun isAccountNonExpired(): Boolean = super.isAccountNonExpired()

    override fun isAccountNonLocked(): Boolean = super.isAccountNonLocked()

    override fun isCredentialsNonExpired(): Boolean = super.isCredentialsNonExpired()

    override fun isEnabled(): Boolean = super.isEnabled()
}