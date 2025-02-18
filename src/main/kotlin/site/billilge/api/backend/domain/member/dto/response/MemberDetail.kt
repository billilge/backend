package site.billilge.api.backend.domain.member.dto.response

import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.member.enums.Role

data class MemberDetail(
    val memberId: Long,
    val name: String,
    val studentId: String,
    val isFeePaid: Boolean,
    val role: Role
) {
    companion object {
        @JvmStatic
        fun from(member: Member): MemberDetail {
            return MemberDetail(
                memberId = member.id!!,
                name = member.name,
                studentId = member.studentId,
                isFeePaid = member.isFeePaid,
                role = member.role
            )
        }
    }
}
