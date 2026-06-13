package site.billilge.api.backend.domain.member.reader

import org.springframework.stereotype.Component
import site.billilge.api.backend.common.exception.ApiException
import site.billilge.api.backend.core.vo.PageRequest
import site.billilge.api.backend.core.vo.PageResult
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.member.enums.Role
import site.billilge.api.backend.domain.member.exception.MemberErrorCode
import site.billilge.api.backend.domain.member.repository.MemberRepository

@Component
class MemberReader(private val memberRepository: MemberRepository) {

    fun read(id: Long): Member =
        memberRepository.findById(id) ?: throw ApiException(MemberErrorCode.MEMBER_NOT_FOUND)

    fun readOrNull(id: Long): Member? = memberRepository.findById(id)

    fun readByEmail(email: String): Member? = memberRepository.findByEmail(email)

    fun readByStudentId(studentId: String): Member? = memberRepository.findByStudentId(studentId)

    fun readByStudentIdAndName(studentId: String, name: String): Member? =
        memberRepository.findByStudentIdAndName(studentId, name)

    fun existsByEmail(email: String): Boolean = memberRepository.existsByEmail(email)

    fun existsByStudentIdAndName(studentId: String, name: String): Boolean =
        memberRepository.existsByStudentIdAndName(studentId, name)

    fun readAllByIds(ids: List<Long>): List<Member> = memberRepository.findAllByIds(ids)

    fun readAllByStudentIds(studentIds: List<String>): List<Member> =
        memberRepository.findAllByStudentIds(studentIds)

    fun readAllByRole(role: Role): List<Member> = memberRepository.findAllByRole(role)

    fun readAllByRoleIn(roles: Set<Role>): List<Member> = memberRepository.findAllByRoleIn(roles)

    fun readAllByRoleInAndNameContaining(roles: List<Role>, name: String, pageRequest: PageRequest): PageResult<Member> =
        memberRepository.findAllByRoleInAndNameContaining(roles, name, pageRequest)

    fun readAllByNameContaining(name: String, pageRequest: PageRequest): PageResult<Member> =
        memberRepository.findAllByNameContaining(name, pageRequest)
}
