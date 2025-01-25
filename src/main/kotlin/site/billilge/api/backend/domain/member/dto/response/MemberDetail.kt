package site.billilge.api.backend.domain.member.dto

import site.billilge.api.backend.domain.member.enums.Department
import site.billilge.api.backend.domain.member.enums.Role

data class MemberDetail(
    val memberId: Long,
    val name: String,
    val email: String,
    val studentId: Int,
    val isFeePaid: Boolean,
    val department: Department,
    val role: Role
)