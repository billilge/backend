package site.billilge.api.backend.global.annotation

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Aspect
@Component
class OnlyAdminAspect {

    @Before("@within(site.billilge.api.backend.global.annotation.OnlyAdmin) || @annotation(site.billilge.api.backend.global.annotation.OnlyAdmin)")
    fun checkRole(joinPoint: JoinPoint) {
        val methodSignature = joinPoint.signature as MethodSignature
        val method = methodSignature.method

        val annotation = method.getAnnotation(OnlyAdmin::class.java)
            ?: joinPoint.target.javaClass.getAnnotation(OnlyAdmin::class.java)
            ?: return

        val allowedRoles = annotation.roles.map { it.key }.toSet()

        val authentication = SecurityContextHolder.getContext().authentication
        val authorities = authentication?.authorities?.map { it.authority }?.toSet() ?: emptySet()

        if (authorities.none { it in allowedRoles }) {
            throw AuthorizationDeniedException("Access Denied")
        }
    }
}
