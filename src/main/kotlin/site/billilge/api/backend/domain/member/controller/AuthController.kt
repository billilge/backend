package site.billilge.api.backend.domain.member.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import site.billilge.api.backend.domain.member.dto.request.SignUpRequest
import site.billilge.api.backend.domain.member.dto.response.SignUpResponse
import site.billilge.api.backend.domain.member.service.MemberService

@RestController
@RequestMapping
class AuthController(
    private val memberService: MemberService
) : AuthApi {
    @PostMapping("/auth/sign-up")
    override fun signUp(@RequestBody request: SignUpRequest): ResponseEntity<SignUpResponse> {
        return ResponseEntity.ok(memberService.signUp(request))
    }

    @GetMapping("/login/oauth2/code/google")
    override fun googleLoginCallback(@RequestParam code: String?): ResponseEntity<Void> {
        return ResponseEntity.ok().build()
    }
}