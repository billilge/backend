package site.billilge.api.backend.domain.member.service

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.billilge.api.backend.domain.member.dto.request.AdminRequest
import site.billilge.api.backend.domain.member.dto.request.SignUpRequest
import site.billilge.api.backend.domain.member.dto.response.AdminFindAllResponse
import site.billilge.api.backend.domain.member.dto.response.AdminMemberDetail
import site.billilge.api.backend.domain.member.dto.response.SignUpResponse
import site.billilge.api.backend.domain.member.exception.MemberErrorCode
import site.billilge.api.backend.domain.member.repository.MemberRepository
import site.billilge.api.backend.global.exception.ApiException
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.member.enums.Role
import site.billilge.api.backend.domain.payer.service.PayerService
import site.billilge.api.backend.global.dto.PageableCondition
import site.billilge.api.backend.global.security.jwt.TokenProvider
import java.time.Duration

@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberRepository: MemberRepository,
    private val tokenProvider: TokenProvider,
    private val payerService: PayerService
) {
    @Transactional
    fun signUp(request: SignUpRequest): SignUpResponse {
        val email = request.email

        if (memberRepository.existsByEmail(email)) {
            throw ApiException(MemberErrorCode.EMAIL_ALREADY_EXISTS)
        }

        val name = request.name
        val studentId = request.studentId

        if (memberRepository.existsByStudentIdAndName(studentId, name)) {
            val member = memberRepository.findByStudentId(studentId)
            member?.updateEmail(email)
        }

        val isFeePaid = payerService.isPayer(name, studentId)

        val member = Member(
            name = name,
            studentId = studentId,
            isFeePaid = isFeePaid
        ).apply {
            updateEmail(email)
        }

        memberRepository.save(member)
        payerService.updatePayerInfo(member)

        val accessToken = tokenProvider.generateToken(member, Duration.ofDays(30))

        return SignUpResponse(accessToken)
    }

    fun getAdminList(pageableCondition: PageableCondition): AdminFindAllResponse {
        val pageRequest = PageRequest.of(
            pageableCondition.pageNo,
            pageableCondition.size,
            Sort.by(Sort.Direction.ASC, "studentId")
        )
        val adminList = memberRepository.findAllByRole(Role.ADMIN, pageRequest)
        val totalPage = adminList.totalPages
        val adminDetails = adminList
            .map { AdminMemberDetail.from(it) }
            .toList()

        return AdminFindAllResponse(adminDetails, totalPage)
    }

    @Transactional
    fun addAdmins(request: AdminRequest) {
        memberRepository.findAllByIds(request.memberIds)
            .forEach { member ->
                member.updateRole(Role.ADMIN)
            }
    }

    @Transactional
    fun deleteAdmins(request: AdminRequest) {
        memberRepository.findAllByIds(request.memberIds)
            .forEach { member ->
                member.updateRole(Role.USER)
            }
    }
}