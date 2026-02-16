package site.billilge.api.backend.domain.member.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import site.billilge.api.backend.domain.member.dto.request.AdminRequest
import site.billilge.api.backend.domain.member.dto.response.AdminFindAllResponse
import site.billilge.api.backend.domain.member.dto.response.MemberFindAllResponse
import site.billilge.api.backend.domain.member.facade.AdminMemberFacade
import site.billilge.api.backend.global.annotation.OnlyAdmin
import site.billilge.api.backend.global.dto.PageableCondition
import site.billilge.api.backend.global.dto.SearchCondition

@RestController
@RequestMapping("admin/members")
@OnlyAdmin
class AdminMemberController(
    private val adminMemberFacade: AdminMemberFacade
) : AdminMemberApi {
    @GetMapping
    override fun getAllMembers(
        @ModelAttribute pageableCondition: PageableCondition,
        @ModelAttribute searchCondition: SearchCondition
    ): ResponseEntity<MemberFindAllResponse> {
        return ResponseEntity.ok(adminMemberFacade.getAllMembers(pageableCondition, searchCondition))
    }

    @GetMapping("/admins")
    override fun getAdminList(
        @ModelAttribute pageableCondition: PageableCondition,
        @ModelAttribute searchCondition: SearchCondition
    ): ResponseEntity<AdminFindAllResponse> {
        return ResponseEntity.ok(adminMemberFacade.getAdminList(pageableCondition, searchCondition))
    }

    @PostMapping("/admins")
    override fun addAdmins(@RequestBody request: AdminRequest): ResponseEntity<Void> {
        adminMemberFacade.addAdmins(request)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/admins")
    override fun deleteAdmins(@RequestBody request: AdminRequest): ResponseEntity<Void> {
        adminMemberFacade.deleteAdmins(request)
        return ResponseEntity.noContent().build()
    }
}