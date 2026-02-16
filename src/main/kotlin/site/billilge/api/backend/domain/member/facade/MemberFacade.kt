package site.billilge.api.backend.domain.member.facade

import org.springframework.stereotype.Component
import site.billilge.api.backend.domain.member.dto.request.MemberFCMTokenRequest
import site.billilge.api.backend.domain.member.service.MemberService

@Component
class MemberFacade(
    private val memberService: MemberService,
) {
    fun setMemberFCMToken(memberId: Long?, request: MemberFCMTokenRequest) {
        memberService.setMemberFCMToken(memberId, request.token)
    }
}
