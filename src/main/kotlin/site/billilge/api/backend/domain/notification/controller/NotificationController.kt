package site.billilge.api.backend.domain.notification.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import site.billilge.api.backend.domain.notification.dto.response.NotificationCountResponse
import site.billilge.api.backend.domain.notification.dto.response.NotificationFindAllResponse
import site.billilge.api.backend.domain.notification.facade.NotificationFacade
import site.billilge.api.backend.global.security.oauth2.UserAuthInfo

@RestController
@RequestMapping("/notifications")
class NotificationController (
    private val notificationFacade: NotificationFacade
): NotificationApi {

    @GetMapping
    override fun getNotifications(
        @AuthenticationPrincipal userAuthInfo: UserAuthInfo
    ): ResponseEntity<NotificationFindAllResponse> {
        val memberId = userAuthInfo.memberId
        return ResponseEntity.ok(notificationFacade.getNotifications(memberId))
    }

    @GetMapping("/count")
    override fun getNotificationCount(
        @AuthenticationPrincipal userAuthInfo: UserAuthInfo
    ): ResponseEntity<NotificationCountResponse> {
        val memberId = userAuthInfo.memberId
        return ResponseEntity.ok(notificationFacade.getNotificationCount(memberId))
    }

    @PatchMapping("/{notificationId}")
    override fun readNotification(
        @AuthenticationPrincipal userAuthInfo: UserAuthInfo,
        @PathVariable notificationId: Long
    ): ResponseEntity<Void> {
        val memberId = userAuthInfo.memberId
        notificationFacade.readNotification(memberId, notificationId)
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/all")
    override fun readAllNotifications(
        @AuthenticationPrincipal userAuthInfo: UserAuthInfo
    ): ResponseEntity<Void> {
        val memberId = userAuthInfo.memberId
        notificationFacade.readAllNotifications(memberId)
        return ResponseEntity.ok().build()
    }
}