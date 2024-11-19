package site.billilge.api.backend.global.security.oauth2

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.security.oauth2.client.ClientsConfiguredCondition
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesMapper
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Conditional
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository


@Configuration
@EnableConfigurationProperties(OAuth2ClientProperties::class)
@Conditional(ClientsConfiguredCondition::class)
class OAuth2ClientRegistrationRepositoryConfig internal constructor(private val properties: OAuth2ClientProperties) {
    @Bean
    @ConditionalOnMissingBean(ClientRegistrationRepository::class)
    fun clientRegistrationRepository(): InMemoryClientRegistrationRepository {
        val registrations: List<ClientRegistration> = OAuth2ClientPropertiesMapper(properties)
            .asClientRegistrations()
            .values
            .toList()

        return InMemoryClientRegistrationRepository(registrations)
    }
}