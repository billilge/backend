package site.billilge.api.backend.domain.item.dto.response

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema

@Schema
data class ItemFindAllResponse(
    @field:ArraySchema(schema = Schema(implementation = ItemDetail::class))
    val items: List<ItemDetail>
)
