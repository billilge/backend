package site.billilge.api.backend.domain.member.service

import org.springframework.stereotype.Service
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.member.repository.MemberRepository

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {
    fun getOrCreateMember(email: String): Member {
        return memberRepository.findByEmail(email)
            ?: Member(email = email).also { memberRepository.save(it) }
    }
}