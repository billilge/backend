package site.billilge.api.backend.domain.member.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import site.billilge.api.backend.domain.member.dto.request.AdminRequest
import site.billilge.api.backend.domain.member.dto.response.AdminFindAllResponse
import site.billilge.api.backend.domain.member.service.MemberService
import site.billilge.api.backend.global.annotation.OnlyAdmin
import site.billilge.api.backend.global.dto.PageableCondition

@RestController
@RequestMapping("admin/members")
@OnlyAdmin
class AdminMemberController(
    private val memberService: MemberService
) : AdminMemberApi {
    @GetMapping("/admins")
    override fun getAdminList(
        @ModelAttribute pageable: PageableCondition
    ): ResponseEntity<AdminFindAllResponse> {
        return ResponseEntity.ok(memberService.getAdminList(pageable.pageNo, pageable.size))
    }

    @PostMapping("/admins")
    fun addAdmins(@RequestBody request: AdminRequest): ResponseEntity<Void> {
        memberService.addAdmins(request)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/admins")
    fun deleteAdmins(@RequestBody request: AdminRequest): ResponseEntity<Void> {
        memberService.deleteAdmins(request)
        return ResponseEntity.noContent().build()
    }
}