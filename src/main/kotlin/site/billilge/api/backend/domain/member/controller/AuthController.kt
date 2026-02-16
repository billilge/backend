package site.billilge.api.backend.domain.member.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import site.billilge.api.backend.domain.member.dto.request.AdminLoginRequest
import site.billilge.api.backend.domain.member.dto.request.SignUpRequest
import site.billilge.api.backend.domain.member.dto.response.AdminLoginResponse
import site.billilge.api.backend.domain.member.dto.response.SignUpResponse
import site.billilge.api.backend.domain.member.facade.AuthFacade

@RestController
@RequestMapping
class AuthController(
    private val authFacade: AuthFacade
) : AuthApi {
    @PostMapping("/auth/sign-up")
    override fun signUp(@RequestBody request: SignUpRequest): ResponseEntity<SignUpResponse> {
        return ResponseEntity.ok(authFacade.signUp(request))
    }

    @PostMapping("/auth/admin-login")
    override fun loginAdmin(@RequestBody request: AdminLoginRequest): ResponseEntity<AdminLoginResponse> {
        return ResponseEntity.ok(authFacade.loginAdmin(request))
    }

    @GetMapping("/login/oauth2/code/google")
    override fun googleLoginCallback(@RequestParam code: String?): ResponseEntity<Void> {
        return ResponseEntity.ok().build()
    }
}