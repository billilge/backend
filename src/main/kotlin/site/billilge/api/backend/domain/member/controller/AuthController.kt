package site.billilge.api.backend.domain.member.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class AuthController {
    @GetMapping("/login/oauth2/code/google")
    fun googleLoginCallback(@RequestParam code: String?): ResponseEntity<Void> {
        return ResponseEntity.ok().build()
    }
}