package site.billilge.api.backend.domain.notification.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import site.billilge.api.backend.domain.notification.api.NotificationApi
import site.billilge.api.backend.domain.notification.dto.response.NotificationFindAllResponse
import site.billilge.api.backend.domain.notification.service.NotificationService
import site.billilge.api.backend.global.security.oauth2.UserAuthInfo

@RestController
@RequestMapping("/notifications")
class NotificationController (
    private val notificationService: NotificationService
): NotificationApi {

    @GetMapping
    override fun getNotifications(
        @AuthenticationPrincipal userAuthInfo: UserAuthInfo
    ): ResponseEntity<NotificationFindAllResponse> {
        val memberId = userAuthInfo.memberId
        return ResponseEntity.ok(notificationService.getNotifications(memberId))
    }
}