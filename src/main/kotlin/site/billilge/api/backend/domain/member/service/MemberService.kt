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
            ?: Member(
                email = email,
                studentId = 20211234,
                name = "황수민"
            ).also { memberRepository.save(it) }
    }
}