package site.billilge.api.backend.domain.member.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.member.enums.Role

interface MemberRepository: JpaRepository<Member, Long> {
    fun findByEmail(email: String): Member?

    fun findByStudentId(studentId: String): Member?

    fun existsByEmail(email: String): Boolean

    fun existsByStudentIdAndName(studentId: String, name: String): Boolean

    @Query("SELECT m FROM Member m WHERE m.role = :role AND m.name LIKE CONCAT('%', :name, '%')")
    fun findAllByRoleAndNameContaining(@Param("role") role: Role, @Param("name") name: String, pageable: Pageable): Page<Member>

    @Query("SELECT m FROM Member m WHERE m.name LIKE CONCAT('%', :name, '%')")
    fun findAllByNameContaining(@Param("name") name: String, pageable: Pageable): Page<Member>

    @Query("select m from Member m where m.id in :ids")
    fun findAllByIds(@Param("ids") ids: List<Long>): List<Member>

    @Query("select m from Member m where m.studentId in :studentIds")
    fun findAllByStudentIds(@Param("studentIds") studentIds: List<String>): List<Member>

    fun findAllByRole(role: Role): List<Member>

    fun findByStudentIdAndName(studentId: String, name: String): Member?
}