package site.billilge.api.backend.domain.member.service

import org.springframework.stereotype.Service
import site.billilge.api.backend.domain.member.dto.request.SignUpRequest
import site.billilge.api.backend.domain.member.dto.response.SignUpResponse
import site.billilge.api.backend.domain.member.exception.MemberErrorCode
import site.billilge.api.backend.domain.member.repository.MemberRepository
import site.billilge.api.backend.global.exception.ApiException
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.global.security.jwt.TokenProvider
import java.time.Duration

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val tokenProvider: TokenProvider
) {
    fun signUp(request: SignUpRequest): SignUpResponse {
        if (memberRepository.existsByEmail(request.email)) {
            throw ApiException(MemberErrorCode.EMAIL_ALREADY_EXISTS)
        }

        val member = Member(
            email = request.email,
            name = request.name,
            studentId = request.studentId,
        )

        memberRepository.save(member)

        val accessToken = tokenProvider.generateToken(member, Duration.ofDays(30))

        return SignUpResponse(accessToken)
    }
}