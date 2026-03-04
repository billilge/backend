package site.billilge.api.backend.domain.configvalue.dto.request

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema

@Schema
data class ConfigValueBulkUpdateRequest(
    @field:ArraySchema(schema = Schema(implementation = ConfigValueUpdateRequest::class))
    val configValues: List<ConfigValueUpdateRequest>,
)
