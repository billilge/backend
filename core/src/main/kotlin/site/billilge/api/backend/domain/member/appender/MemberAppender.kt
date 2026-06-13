package site.billilge.api.backend.domain.member.appender

import org.springframework.stereotype.Component
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.member.repository.MemberRepository

@Component
class MemberAppender(private val memberRepository: MemberRepository) {

    fun save(member: Member): Member = memberRepository.save(member)

    fun saveAll(members: List<Member>): List<Member> =
        members.map { memberRepository.save(it) }
}
