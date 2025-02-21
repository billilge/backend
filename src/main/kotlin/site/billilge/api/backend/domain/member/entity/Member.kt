package site.billilge.api.backend.domain.member.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import site.billilge.api.backend.domain.member.enums.Department
import site.billilge.api.backend.domain.member.enums.Role
import java.time.LocalDateTime

@Entity
@Table(name = "member")
@EntityListeners(AuditingEntityListener::class)
class Member(
    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "student_id", nullable = false, unique = true)
    val studentId: String,

    @Column(name = "is_fee_paid", nullable = false, columnDefinition = "TINYINT(1)")
    var isFeePaid: Boolean = false,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    val id: Long? = null

    @Column(name = "email")
    var email: String? = null
        protected set

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var department: Department = Department.SW
        protected set

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var role: Role = Role.USER
        protected set

    @Column(name = "fcm_token")
    var fcmToken: String? = null
        protected set

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
        protected set

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
        protected set

    fun updateRole(role: Role) {
        this.role = role
    }

    fun updateEmail(email: String) {
        this.email = email
    }

    fun updateFCMToken(fcmToken: String) {
        this.fcmToken = fcmToken
    }
}