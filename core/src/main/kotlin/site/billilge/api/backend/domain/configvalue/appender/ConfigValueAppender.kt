package site.billilge.api.backend.domain.configvalue.appender

import org.springframework.stereotype.Component
import site.billilge.api.backend.domain.configvalue.entity.ConfigValue
import site.billilge.api.backend.domain.configvalue.repository.ConfigValueRepository

@Component
class ConfigValueAppender(private val configValueRepository: ConfigValueRepository) {

    fun save(configValue: ConfigValue): ConfigValue = configValueRepository.save(configValue)
}
