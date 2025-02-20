package site.billilge.api.backend.domain.notification.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
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
}