package site.billilge.api.backend.global.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@OpenAPIDefinition(
    info = Info(
        title = "빌릴게(Billilge) API",
        description = "국민대학교 소프트웨어융합대학 복지물품 대여 서비스",
        version = "v1"
    )
)
@Configuration
class SwaggerConfig(
    @Value("\${swagger.server.base-url}")
    private val serverBaseUrl: String,
) {
    @Bean
    fun openAPI(): OpenAPI {
        val securityRequirement = SecurityRequirement().addList("JWT")
        val components = Components().addSecuritySchemes(
            "JWT", SecurityScheme()
                .name("JWT")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        )

        val server = Server()
            .apply { url = serverBaseUrl }

        return OpenAPI()
            .components(Components())
            .addSecurityItem(securityRequirement)
            .servers(listOf(server))
            .components(components)
    }
}