package site.billilge.api.backend.domain.notification.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import site.billilge.api.backend.domain.notification.dto.response.NotificationFindAllResponse
import site.billilge.api.backend.domain.notification.service.NotificationService
import site.billilge.api.backend.global.security.oauth2.UserAuthInfo

@RestController
@RequestMapping("/admin/notifications")
class AdminNotificationController(
    private val notificationService: NotificationService
) : AdminNotificationApi {
    @GetMapping
    override fun getAdminNotifications(@AuthenticationPrincipal userAuthInfo: UserAuthInfo): ResponseEntity<NotificationFindAllResponse> {
        return ResponseEntity.ok(notificationService.getAdminNotifications(userAuthInfo.memberId))
    }
}