package site.billilge.api.backend.domain.configvalue.reader

import org.springframework.stereotype.Component
import site.billilge.api.backend.common.exception.ApiException
import site.billilge.api.backend.domain.configvalue.entity.ConfigValue
import site.billilge.api.backend.domain.configvalue.exception.ConfigValueErrorCode
import site.billilge.api.backend.domain.configvalue.repository.ConfigValueRepository

@Component
class ConfigValueReader(private val configValueRepository: ConfigValueRepository) {

    fun read(key: String): ConfigValue =
        configValueRepository.findByKey(key) ?: throw ApiException(ConfigValueErrorCode.CONFIG_VALUE_NOT_FOUND)

    fun readAllByKeys(keys: List<String>): List<ConfigValue> =
        configValueRepository.findAllByKeyIn(keys)
}
