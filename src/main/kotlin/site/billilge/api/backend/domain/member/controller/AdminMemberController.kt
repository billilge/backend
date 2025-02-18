package site.billilge.api.backend.domain.member.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import site.billilge.api.backend.domain.member.dto.request.AdminRequest
import site.billilge.api.backend.domain.member.dto.response.AdminFindAllResponse
import site.billilge.api.backend.domain.member.dto.response.MemberFindAllResponse
import site.billilge.api.backend.domain.member.service.MemberService
import site.billilge.api.backend.global.annotation.OnlyAdmin
import site.billilge.api.backend.global.dto.PageableCondition
import site.billilge.api.backend.global.dto.SearchCondition

@RestController
@RequestMapping("admin/members")
@OnlyAdmin
class AdminMemberController(
    private val memberService: MemberService
) : AdminMemberApi {

    @GetMapping
    override fun getAllMembers(
        @ModelAttribute pageableCondition: PageableCondition,
        @ModelAttribute searchCondition: SearchCondition
    ): ResponseEntity<MemberFindAllResponse> {
        return ResponseEntity.ok(memberService.getAllMembers(pageableCondition, searchCondition))
    }

    @GetMapping("/admins")
    override fun getAdminList(
        @ModelAttribute pageableCondition: PageableCondition,
        @ModelAttribute searchCondition: SearchCondition
    ): ResponseEntity<AdminFindAllResponse> {
        return ResponseEntity.ok(memberService.getAdminList(pageableCondition, searchCondition))
    }

    @PostMapping("/admins")
    override fun addAdmins(@RequestBody request: AdminRequest): ResponseEntity<Void> {
        memberService.addAdmins(request)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/admins")
    override fun deleteAdmins(@RequestBody request: AdminRequest): ResponseEntity<Void> {
        memberService.deleteAdmins(request)
        return ResponseEntity.noContent().build()
    }
}