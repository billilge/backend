package site.billilge.api.backend.domain.member.repository

import org.springframework.data.jpa.repository.JpaRepository
import site.billilge.api.backend.domain.member.entity.Member

interface MemberRepository: JpaRepository<Member, Long> {
    fun findByEmail(email: String): Member?
}