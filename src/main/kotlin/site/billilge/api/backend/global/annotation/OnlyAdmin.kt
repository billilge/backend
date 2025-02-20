package site.billilge.api.backend.global.annotation

import org.springframework.security.access.prepost.PreAuthorize

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.CLASS
)
@Retention(
    AnnotationRetention.RUNTIME
)
@PreAuthorize("hasRole('ROLE_ADMIN')")
annotation class OnlyAdmin