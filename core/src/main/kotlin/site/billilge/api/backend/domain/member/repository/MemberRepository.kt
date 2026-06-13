package site.billilge.api.backend.domain.member.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.member.enums.Role

interface MemberRepository {
    fun findById(id: Long): Member?
    fun findByEmail(email: String): Member?
    fun findByStudentId(studentId: String): Member?
    fun existsByEmail(email: String): Boolean
    fun existsByStudentIdAndName(studentId: String, name: String): Boolean
    fun save(member: Member): Member
    fun findAllByRoleAndNameContaining(role: Role, name: String, pageable: Pageable): Page<Member>
    fun findAllByRoleInAndNameContaining(roles: List<Role>, name: String, pageable: Pageable): Page<Member>
    fun findAllByNameContaining(name: String, pageable: Pageable): Page<Member>
    fun findAllByIds(ids: List<Long>): List<Member>
    fun findAllByStudentIds(studentIds: List<String>): List<Member>
    fun findAllByRole(role: Role): List<Member>
    fun findByStudentIdAndName(studentId: String, name: String): Member?
    fun findAllByRoleIn(roles: Set<Role>): List<Member>
}
