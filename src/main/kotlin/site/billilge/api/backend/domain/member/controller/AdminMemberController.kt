package site.billilge.api.backend.domain.member.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import site.billilge.api.backend.domain.member.dto.response.AdminFindAllResponse
import site.billilge.api.backend.domain.member.service.MemberService
import site.billilge.api.backend.global.annotation.OnlyAdmin

@RestController
@RequestMapping("admin/members")
@OnlyAdmin
class AdminMemberController(
    private val memberService: MemberService
) : AdminMemberApi {
    @GetMapping("/admins")
    override fun getAdminList(): ResponseEntity<AdminFindAllResponse> {
        return ResponseEntity.ok(memberService.getAdminList())
    }
}