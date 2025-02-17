package site.billilge.api.backend.domain.member.dto.response

import site.billilge.api.backend.domain.member.entity.Member

data class AdminMemberDetail(
    val memberId: Long,
    val name: String,
    val studentId: String
) {
    companion object {
        @JvmStatic
        fun from(member: Member): AdminMemberDetail {
            return AdminMemberDetail(
                memberId = member.id!!,
                name = member.name,
                studentId = member.studentId
            )
        }
    }
}