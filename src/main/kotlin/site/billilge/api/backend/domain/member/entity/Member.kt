package site.billilge.api.backend.domain.member.entity

import jakarta.persistence.*
import site.billilge.api.backend.domain.member.enums.Role

@Entity
@Table(name = "member")
class Member(
    @Column(name = "email", unique = true, nullable = false)
    val email: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id = 0L

    @Column(nullable = true)
    var name: String? = null

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var role: Role = Role.USER
        protected set

    fun updateRole(role: Role) {
        this.role = role
    }
}