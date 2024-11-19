package site.billilge.api.backend.global.security

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import site.billilge.api.backend.domain.member.repository.MemberRepository
import site.billilge.api.backend.global.exception.ApiException
import site.billilge.api.backend.global.exception.GlobalErrorCode
import site.billilge.api.backend.global.security.oauth2.UserAuthInfo

@Service
class UserAuthInfoService(
    private val memberRepository: MemberRepository
): UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) {
            throw UsernameNotFoundException("Username not found")
        }

        val member = memberRepository.findByEmail(username)
            ?: throw ApiException(GlobalErrorCode.INTERNAL_SERVER_ERROR)

        return UserAuthInfo(member)
    }
}