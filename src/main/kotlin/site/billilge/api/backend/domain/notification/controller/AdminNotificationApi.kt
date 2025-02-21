package site.billilge.api.backend.domain.notification.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import site.billilge.api.backend.domain.notification.dto.response.NotificationFindAllResponse
import site.billilge.api.backend.global.security.oauth2.UserAuthInfo

@Tag(name = "(Admin) Notification", description = "관리자용 알림 API")
interface AdminNotificationApi {
    @Operation(
        summary = "관리자 알림 목록 조회",
        description = "로그인한 사용자의 관리자 알림 목록을 최신순으로 조회합니다."
    )
    @ApiResponses(
        value =  [
            ApiResponse(
                responseCode =  "200",
                description = "관리자 알림 목록 조회 성공"
            )
        ]
    )
    fun getAdminNotifications(@AuthenticationPrincipal userAuthInfo: UserAuthInfo): ResponseEntity<NotificationFindAllResponse>
}