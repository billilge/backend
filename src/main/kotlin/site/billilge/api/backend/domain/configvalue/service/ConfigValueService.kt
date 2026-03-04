package site.billilge.api.backend.domain.configvalue.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.billilge.api.backend.domain.configvalue.entity.ConfigValue
import site.billilge.api.backend.domain.configvalue.enums.ConfigValueKeys
import site.billilge.api.backend.domain.configvalue.exception.ConfigValueErrorCode
import site.billilge.api.backend.domain.configvalue.repository.ConfigValueRepository
import site.billilge.api.backend.global.exception.ApiException

@Service
@Transactional(readOnly = true)
class ConfigValueService(
    private val configValueRepository: ConfigValueRepository,
) {
    fun getByKey(key: String): ConfigValue {
        return configValueRepository.findByKey(key)
            .orElseThrow { ApiException(ConfigValueErrorCode.CONFIG_VALUE_NOT_FOUND) }
    }

    fun getValueByKey(key: String): String {
        return getByKey(key).value
    }

    fun getAllByKeys(keys: List<String>): List<ConfigValue> {
        return configValueRepository.findAllByKeyIn(keys)
    }

    fun getMapByKeys(keys: List<String>): Map<String, String> = getAllByKeys(keys).associateBy(
        { it.key },
        { it.value }
    )

    @Transactional
    fun upsert(key: String, value: String) {
        val configValue = configValueRepository.findByKey(key)
        if (configValue.isPresent) {
            configValue.get().updateValue(value)
        } else {
            configValueRepository.save(ConfigValue(key = key, value = value))
        }
    }

    @Transactional
    fun changeAdminPassword(currentPassword: String, newPassword: String) {
        val configValue = getByKey(ConfigValueKeys.ADMIN_PASSWORD.key)
        if (configValue.value != currentPassword) {
            throw ApiException(ConfigValueErrorCode.ADMIN_PASSWORD_MISMATCH)
        }
        configValue.updateValue(newPassword)
    }
}
