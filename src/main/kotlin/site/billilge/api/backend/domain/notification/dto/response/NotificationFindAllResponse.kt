package site.billilge.api.backend.domain.notification.dto.response

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema

@Schema
data class NotificationFindAllResponse(
    @field:ArraySchema(schema = Schema(implementation = NotificationDetail::class))
    val messages: List<NotificationDetail>
)
