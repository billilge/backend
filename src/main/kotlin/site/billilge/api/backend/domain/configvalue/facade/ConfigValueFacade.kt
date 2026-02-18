package site.billilge.api.backend.domain.configvalue.facade

import org.springframework.stereotype.Component
import site.billilge.api.backend.domain.configvalue.dto.request.ConfigValueBulkUpdateRequest
import site.billilge.api.backend.domain.configvalue.dto.request.ConfigValueUpdateRequest
import site.billilge.api.backend.domain.configvalue.dto.response.ConfigValueDetail
import site.billilge.api.backend.domain.configvalue.dto.response.ConfigValueFindAllResponse
import site.billilge.api.backend.domain.configvalue.service.ConfigValueService

@Component
class ConfigValueFacade(
    private val configValueService: ConfigValueService,
) {
    fun getByKey(key: String): ConfigValueDetail {
        return ConfigValueDetail.from(configValueService.getByKey(key))
    }

    fun getAllByKeys(keys: List<String>): ConfigValueFindAllResponse {
        val details = configValueService.getAllByKeys(keys).map { ConfigValueDetail.from(it) }
        return ConfigValueFindAllResponse(details)
    }

    fun update(request: ConfigValueUpdateRequest) {
        configValueService.upsert(request.key, request.value)
    }

    fun updateAll(request: ConfigValueBulkUpdateRequest) {
        request.configValues.forEach { configValueService.upsert(it.key, it.value) }
    }
}
