package site.billilge.api.backend.domain.member.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.billilge.api.backend.domain.member.dto.request.SignUpRequest
import site.billilge.api.backend.domain.member.dto.response.SignUpResponse
import site.billilge.api.backend.domain.member.exception.MemberErrorCode
import site.billilge.api.backend.domain.member.repository.MemberRepository
import site.billilge.api.backend.global.exception.ApiException
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.member.enums.Role
import site.billilge.api.backend.domain.payer.service.PayerService
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
}