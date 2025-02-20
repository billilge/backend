package site.billilge.api.backend.domain.member.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import site.billilge.api.backend.domain.member.entity.Member

@Schema
data class AdminMemberDetail(
    @field:Schema(description = "관리자 회원 ID", example = "1")
    val memberId: Long,
    @field:Schema(description = "관리자 이름", example = "황수민")
    val name: String,
    @field:Schema(description = "관리자 학번", example = "20211234")
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