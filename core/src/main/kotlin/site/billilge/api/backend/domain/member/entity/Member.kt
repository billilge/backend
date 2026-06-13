package site.billilge.api.backend.domain.member.entity

import site.billilge.api.backend.domain.member.enums.Department
import site.billilge.api.backend.domain.member.enums.Role
import java.time.LocalDateTime

data class Member(
    val id: Long?,
    val name: String,
    val studentId: String,
    val isFeePaid: Boolean = false,
    val email: String? = null,
    val department: Department = Department.SW,
    val role: Role = Role.USER,
    val fcmToken: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)
