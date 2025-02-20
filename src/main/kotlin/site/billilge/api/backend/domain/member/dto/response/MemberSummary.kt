package site.billilge.api.backend.domain.member.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import site.billilge.api.backend.domain.member.entity.Member

@Schema
data class MemberSummary(
    @field:Schema(description = "회원 이름", example = "황수민")
    val name: String,
    @field:Schema(description = "회원 학번", example = "20211234")
    val studentId: String
) {
    companion object {
        @JvmStatic
        fun from(member: Member): MemberSummary {
            return MemberSummary(
                name = member.name,
                studentId = member.studentId
            )
        }
    }
}
