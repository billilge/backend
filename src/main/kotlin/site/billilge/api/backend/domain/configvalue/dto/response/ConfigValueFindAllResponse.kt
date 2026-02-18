package site.billilge.api.backend.domain.configvalue.dto.response

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema

@Schema
data class ConfigValueFindAllResponse(
    @field:ArraySchema(schema = Schema(implementation = ConfigValueDetail::class))
    val configValues: List<ConfigValueDetail>,
)
