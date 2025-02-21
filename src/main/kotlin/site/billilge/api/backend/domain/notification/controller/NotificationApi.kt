package site.billilge.api.backend.domain.notification.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import site.billilge.api.backend.domain.notification.dto.response.NotificationCountResponse
import site.billilge.api.backend.domain.notification.dto.response.NotificationFindAllResponse
import site.billilge.api.backend.global.security.oauth2.UserAuthInfo

@Tag(name = "Notification", description = "알림 API")
interface NotificationApi {

    @Operation(
        summary = "사용자의 알림 목록 조회",
        description = "로그인한 사용자의 알림 목록을 최신순으로 조회합니다."
    )
    @ApiResponses(
        value =  [
            ApiResponse(
                responseCode =  "200",
                description = "알림 목록 조회 성공"
            )
        ]
    )
    fun getNotifications(
        @AuthenticationPrincipal userAuthInfo: UserAuthInfo
    ): ResponseEntity<NotificationFindAllResponse>


    @Operation(
        summary = "알림 읽음 처리",
        description = "사용자가 특정 알림을 읽음 처리합니다."
    )
    @ApiResponses(
        value =  [
            ApiResponse(responseCode = "200", description = "알림 읽음 처리 완료"),
            ApiResponse(responseCode = "403", description = "권한이 없는 사용자"),
            ApiResponse(responseCode = "404", description = "알림을 찾을 수 없음")
        ]
    )
    fun readNotification(
        @AuthenticationPrincipal userAuthInfo: UserAuthInfo,
        @PathVariable notificationId: Long
    ): ResponseEntity<Void>

    @Operation(
        summary = "알림 개수 조회",
        description = "로그인한 사용자의 알림 개수를 조회합니다. (메인 페이지)"
    )
    @ApiResponses(
        value =  [
            ApiResponse(
                responseCode =  "200",
                description = "개수 조회 성공"
            )
        ]
    )
    fun getNotificationCount(
        @AuthenticationPrincipal userAuthInfo: UserAuthInfo
    ): ResponseEntity<NotificationCountResponse>
}