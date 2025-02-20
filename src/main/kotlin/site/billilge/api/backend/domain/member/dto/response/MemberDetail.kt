package site.billilge.api.backend.domain.member.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.member.enums.Role

@Schema
data class MemberDetail(
    @field:Schema(description = "회원 ID", example = "1")
    val memberId: Long,
    @field:Schema(description = "회원 이름", example = "황수민")
    val name: String,
    @field:Schema(description = "회원 학번", example = "20211234")
    val studentId: String,
    @field:Schema(description = "회원 학생회비 납부 여부", example = "false")
    val isFeePaid: Boolean,
    @field:Schema(description = "회원 권한", example = "USER")
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
