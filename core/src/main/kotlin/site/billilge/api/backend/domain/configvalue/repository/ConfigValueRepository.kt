package site.billilge.api.backend.domain.configvalue.repository

import site.billilge.api.backend.domain.configvalue.entity.ConfigValue

interface ConfigValueRepository {
    fun findById(id: Long): ConfigValue?
    fun findByKey(key: String): ConfigValue?
    fun findAllByKeyIn(keys: List<String>): List<ConfigValue>
    fun save(configValue: ConfigValue): ConfigValue
}
