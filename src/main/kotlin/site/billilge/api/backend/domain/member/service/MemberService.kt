package site.billilge.api.backend.domain.member.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Value
import site.billilge.api.backend.domain.member.exception.MemberErrorCode
import site.billilge.api.backend.domain.member.repository.MemberRepository
import site.billilge.api.backend.global.exception.ApiException
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.member.enums.Role
import site.billilge.api.backend.domain.payer.service.PayerService
import site.billilge.api.backend.global.dto.PageableCondition
import site.billilge.api.backend.global.dto.SearchCondition
import site.billilge.api.backend.global.security.jwt.TokenProvider
import java.time.Duration

@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberRepository: MemberRepository,
    private val tokenProvider: TokenProvider,
    private val payerService: PayerService,
    @Value("\${login.admin-password}")
    private val adminPassword: String,
) {
    @Transactional
    fun signUp(email: String, studentId: String, name: String): String {
        if (memberRepository.existsByEmail(email)) {
            throw ApiException(MemberErrorCode.EMAIL_ALREADY_EXISTS)
        }

        if (memberRepository.existsByStudentIdAndName(studentId, name)) {
            val member = memberRepository.findByStudentId(studentId)

            member?.let {
                it.updateEmail(email)

                return tokenProvider.generateToken(it, Duration.ofDays(30))
            }
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

        return tokenProvider.generateToken(member, Duration.ofDays(30))
    }

    fun getAdminList(pageableCondition: PageableCondition, searchCondition: SearchCondition): Page<Member> {
        val pageRequest = PageRequest.of(
            pageableCondition.pageNo,
            pageableCondition.size,
            Sort.by(Sort.Direction.ASC, "studentId")
        )
        return memberRepository.findAllByRoleAndNameContaining(Role.ADMIN, searchCondition.search, pageRequest)
    }

    @Transactional
    fun addAdmins(memberIds: List<Long>) {
        memberRepository.findAllByIds(memberIds)
            .forEach { member ->
                member.updateRole(Role.ADMIN)
            }
    }

    @Transactional
    fun deleteAdmins(memberIds: List<Long>) {
        memberRepository.findAllByIds(memberIds)
            .forEach { member ->
                member.updateRole(Role.USER)
            }
    }

    fun getAllMembers(
        pageableCondition: PageableCondition,
        searchCondition: SearchCondition
    ): Page<Member> {
        val pageRequest = PageRequest.of(
            pageableCondition.pageNo,
            pageableCondition.size,
            Sort.by(Sort.Direction.ASC, "studentId")
        )

        return if (searchCondition.search.isEmpty()) {
            memberRepository.findAll(pageRequest)
        } else {
            memberRepository.findAllByNameContaining(searchCondition.search, pageRequest)
        }
    }

    @Transactional
    fun setMemberFCMToken(memberId: Long?, token: String) {
        val member = memberRepository.findByIdOrNull(memberId!!)
            ?: throw ApiException(MemberErrorCode.MEMBER_NOT_FOUND)

        member.updateFCMToken(token)
    }

    fun findById(memberId: Long): Member {
        return memberRepository.findByIdOrNull(memberId)
            ?: throw ApiException(MemberErrorCode.MEMBER_NOT_FOUND)
    }

    fun findAllByRole(role: Role): List<Member> {
        return memberRepository.findAllByRole(role)
    }

    fun loginAdmin(studentId: String, password: String): String {
        val member = memberRepository.findByStudentId(studentId)
            ?: throw ApiException(MemberErrorCode.MEMBER_NOT_FOUND)

        if (password != adminPassword)
            throw ApiException(MemberErrorCode.ADMIN_PASSWORD_MISMATCH)

        if (member.role !in listOf(Role.ADMIN, Role.GA, Role.WORKER))
            throw ApiException(MemberErrorCode.FORBIDDEN)

        return tokenProvider.generateToken(member, Duration.ofDays(30))
    }
}
