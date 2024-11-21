package site.billilge.api.backend.domain.admin.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import site.billilge.api.backend.global.annotation.OnlyAdmin

@RestController
@RequestMapping("/admin")
@OnlyAdmin
class AdminController {

}