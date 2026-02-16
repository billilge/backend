package site.billilge.api.backend.domain.member.facade

import org.springframework.stereotype.Component
import site.billilge.api.backend.domain.member.dto.request.AdminRequest
import site.billilge.api.backend.domain.member.dto.response.AdminFindAllResponse
import site.billilge.api.backend.domain.member.dto.response.AdminMemberDetail
import site.billilge.api.backend.domain.member.dto.response.MemberDetail
import site.billilge.api.backend.domain.member.dto.response.MemberFindAllResponse
import site.billilge.api.backend.domain.member.service.MemberService
import site.billilge.api.backend.global.dto.PageableCondition
import site.billilge.api.backend.global.dto.SearchCondition

@Component
class AdminMemberFacade(
    private val memberService: MemberService,
) {
    fun getAllMembers(pageableCondition: PageableCondition, searchCondition: SearchCondition): MemberFindAllResponse {
        val members = memberService.getAllMembers(pageableCondition, searchCondition)
        val memberDetails = members.map { MemberDetail.from(it) }.toList()
        return MemberFindAllResponse(memberDetails, members.totalPages)
    }

    fun getAdminList(pageableCondition: PageableCondition, searchCondition: SearchCondition): AdminFindAllResponse {
        val adminList = memberService.getAdminList(pageableCondition, searchCondition)
        val adminDetails = adminList.map { AdminMemberDetail.from(it) }.toList()
        return AdminFindAllResponse(adminDetails, adminList.totalPages)
    }

    fun addAdmins(request: AdminRequest) {
        memberService.addAdmins(request.memberIds)
    }

    fun deleteAdmins(request: AdminRequest) {
        memberService.deleteAdmins(request.memberIds)
    }
}
