package site.billilge.api.backend.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import site.billilge.api.backend.global.security.jwt.TokenAuthenticationFilter
import site.billilge.api.backend.global.security.jwt.TokenProvider
import site.billilge.api.backend.global.security.oauth2.CustomOAuth2UserService
import site.billilge.api.backend.global.security.oauth2.OAuth2AuthenticationFailureHandler
import site.billilge.api.backend.global.security.oauth2.OAuth2AuthenticationSuccessHandler

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val tokenProvider: TokenProvider,
    private val customOAuth2UserService: CustomOAuth2UserService,
    private val oAuth2AuthenticationSuccessHandler: OAuth2AuthenticationSuccessHandler,
    private val oAuth2AuthenticationFailureHandler: OAuth2AuthenticationFailureHandler,
) {
    @Bean
    fun tokenAuthenticationFilter(): TokenAuthenticationFilter {
        return TokenAuthenticationFilter(tokenProvider)
    }

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        httpSecurity
            .oauth2Login { oauth2 ->
                oauth2
                    .authorizationEndpoint(Customizer { authorization ->
                        authorization.baseUri("/oauth2/authorization")
                    })
                    .redirectionEndpoint(Customizer { redirection ->
                        redirection.baseUri("/*/oauth2/code/*")
                    })
                    .userInfoEndpoint(Customizer { userInfo ->
                        userInfo.userService(customOAuth2UserService)
                    })
                    .successHandler(oAuth2AuthenticationSuccessHandler)
                    .failureHandler(oAuth2AuthenticationFailureHandler)
            }
            .cors { cors -> cors.configurationSource(apiConfigurationSource()) }
            .csrf { csrf -> csrf.disable() }
            .formLogin { formLogin -> formLogin.disable() }
            .httpBasic { httpBasic -> httpBasic.disable() }
            .sessionManagement { sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests { httpRequests ->
                httpRequests
                    .requestMatchers(*SWAGGER_API_PATH).permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)

        return httpSecurity.build()
    }

    @Bean
    fun apiConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("*")
        configuration.allowedMethods = mutableListOf("GET", "POST", "PATCH", "PUT", "DELETE")
        configuration.allowedHeaders = mutableListOf("Content-Type", "Authorization")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)

        return source
    }

    companion object {
        @JvmStatic
        val SWAGGER_API_PATH: Array<String> = arrayOf( //swagger
            "/v3/api-docs/**",
            "/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**"
        )
    }
}