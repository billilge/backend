package site.billilge.api.backend.global.annotation

import site.billilge.api.backend.domain.member.enums.Role

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.CLASS
)
@Retention(
    AnnotationRetention.RUNTIME
)
annotation class OnlyAdmin(
    val roles: Array<Role> = [Role.ADMIN]
)
