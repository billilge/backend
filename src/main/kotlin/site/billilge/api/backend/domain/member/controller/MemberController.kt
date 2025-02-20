package site.billilge.api.backend.domain.member.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import site.billilge.api.backend.domain.member.dto.request.MemberFCMTokenRequest
import site.billilge.api.backend.domain.member.service.MemberService
import site.billilge.api.backend.global.security.oauth2.UserAuthInfo

@RestController
@RequestMapping("/members")
class MemberController(
    private val memberService: MemberService,
) : MemberApi {
    @PostMapping("/me/fcm-token")
    override fun setFCMToken(
        @AuthenticationPrincipal userAuthInfo: UserAuthInfo,
        @RequestBody request: MemberFCMTokenRequest
    ): ResponseEntity<Void> {
        memberService.setMemberFCMToken(userAuthInfo.memberId, request)
        return ResponseEntity.ok().build()
    }
}