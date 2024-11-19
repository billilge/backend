package site.billilge.api.backend.global.security.oauth2

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User
import site.billilge.api.backend.domain.member.entity.Member

class UserAuthInfo: OAuth2User, UserDetails {
    private val authorities = mutableListOf<GrantedAuthority>()
    private var attributes = hashMapOf<String, Any?>()

    constructor(oAuth2User: OAuth2User) {
        authorities.addAll(oAuth2User.authorities)
        attributes.putAll(oAuth2User.attributes)
//        println("attributes: $attributes")
//        println("email: ${attributes["email"]}")
    }

    constructor(member: Member) {
        authorities.add(SimpleGrantedAuthority(member.role.key))
    }

    val email
        get() = attributes["email"] as? String ?: ""

    override fun getName(): String? = attributes["name"] as String?

    override fun getAttributes(): MutableMap<String, Any?> = attributes

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authorities

    override fun getPassword(): String = ""

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = super.isAccountNonExpired()

    override fun isAccountNonLocked(): Boolean = super.isAccountNonLocked()

    override fun isCredentialsNonExpired(): Boolean = super.isCredentialsNonExpired()

    override fun isEnabled(): Boolean = super.isEnabled()
}