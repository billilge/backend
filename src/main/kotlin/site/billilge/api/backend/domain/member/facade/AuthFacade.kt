package site.billilge.api.backend.domain.member.facade

import org.springframework.stereotype.Component
import site.billilge.api.backend.domain.member.dto.request.AdminLoginRequest
import site.billilge.api.backend.domain.member.dto.request.SignUpRequest
import site.billilge.api.backend.domain.member.dto.response.AdminLoginResponse
import site.billilge.api.backend.domain.member.dto.response.SignUpResponse
import site.billilge.api.backend.domain.member.service.MemberService

@Component
class AuthFacade(
    private val memberService: MemberService,
) {
    fun signUp(request: SignUpRequest): SignUpResponse {
        val accessToken = memberService.signUp(request.email, request.studentId, request.name)
        return SignUpResponse(accessToken)
    }

    fun loginAdmin(request: AdminLoginRequest): AdminLoginResponse {
        val accessToken = memberService.loginAdmin(request.studentId, request.password)
        return AdminLoginResponse(accessToken)
    }
}
