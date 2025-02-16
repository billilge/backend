package site.billilge.api.backend.domain.member.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.member.enums.Role

interface MemberRepository: JpaRepository<Member, Long> {
    fun findByEmail(email: String): Member?

    fun findByStudentId(studentId: String): Member?

    fun existsByEmail(email: String): Boolean

    fun existsByStudentIdAndName(studentId: String, name: String): Boolean

    fun findAllByRole(role: Role, pageable: Pageable): Page<Member>
}