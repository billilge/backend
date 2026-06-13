package site.billilge.api.backend.domain.configvalue.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.billilge.api.backend.common.exception.ApiException
import site.billilge.api.backend.domain.configvalue.appender.ConfigValueAppender
import site.billilge.api.backend.domain.configvalue.entity.ConfigValue
import site.billilge.api.backend.domain.configvalue.exception.ConfigValueErrorCode
import site.billilge.api.backend.domain.configvalue.reader.ConfigValueReader

@Service
@Transactional(readOnly = true)
class ConfigValueService(
    private val configValueReader: ConfigValueReader,
    private val configValueAppender: ConfigValueAppender,
) {
    fun getByKey(key: String): ConfigValue = configValueReader.read(key)

    fun getValueByKey(key: String): String = configValueReader.read(key).value

    fun getMapByKeys(keys: List<String>): Map<String, String> =
        configValueReader.readAllByKeys(keys).associate { it.key to it.value }

    @Transactional
    fun upsert(key: String, value: String) {
        val existing = configValueReader.readAllByKeys(listOf(key)).firstOrNull()
        if (existing != null) {
            configValueAppender.save(existing.copy(value = value))
        } else {
            configValueAppender.save(ConfigValue(id = null, key = key, value = value))
        }
    }

    @Transactional
    fun changeAdminPassword(currentPassword: String, newPassword: String) {
        val configValue = configValueReader.read(site.billilge.api.backend.domain.configvalue.enums.ConfigValueKeys.ADMIN_PASSWORD.key)
        if (configValue.value != currentPassword) throw ApiException(ConfigValueErrorCode.ADMIN_PASSWORD_MISMATCH)
        configValueAppender.save(configValue.copy(value = newPassword))
    }
}
