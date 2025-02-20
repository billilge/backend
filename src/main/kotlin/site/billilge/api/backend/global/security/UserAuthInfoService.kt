package site.billilge.api.backend.global.security

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import site.billilge.api.backend.domain.member.exception.MemberErrorCode
import site.billilge.api.backend.domain.member.repository.MemberRepository
import site.billilge.api.backend.global.exception.ApiException
import site.billilge.api.backend.global.security.oauth2.UserAuthInfo

@Service
class UserAuthInfoService(
    private val memberRepository: MemberRepository
): UserDetailsService {
    override fun loadUserByUsername(studentId: String?): UserDetails {
        if (studentId == null) {
            throw UsernameNotFoundException("Member's studentId not found")
        }

        val member = memberRepository.findByStudentId(studentId)
            ?: throw ApiException(MemberErrorCode.MEMBER_NOT_FOUND)

        return UserAuthInfo(member)
    }
}