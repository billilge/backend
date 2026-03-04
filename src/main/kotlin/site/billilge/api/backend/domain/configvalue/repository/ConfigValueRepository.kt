package site.billilge.api.backend.domain.configvalue.repository

import org.springframework.data.jpa.repository.JpaRepository
import site.billilge.api.backend.domain.configvalue.entity.ConfigValue
import java.util.*

interface ConfigValueRepository : JpaRepository<ConfigValue, Long> {
    fun findByKey(key: String): Optional<ConfigValue>
    fun findAllByKeyIn(keys: List<String>): List<ConfigValue>
}
