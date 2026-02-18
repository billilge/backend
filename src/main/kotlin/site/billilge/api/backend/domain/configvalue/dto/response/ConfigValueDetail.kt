package site.billilge.api.backend.domain.configvalue.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import site.billilge.api.backend.domain.configvalue.entity.ConfigValue

@Schema
data class ConfigValueDetail(
    @field:Schema(description = "설정 키", example = "exam-period.start-date")
    val key: String,
    @field:Schema(description = "설정 값", example = "2025-04-14")
    val value: String,
) {
    companion object {
        @JvmStatic
        fun from(configValue: ConfigValue): ConfigValueDetail {
            return ConfigValueDetail(
                key = configValue.key,
                value = configValue.value,
            )
        }
    }
}
