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
    @Column(name = "email", unique = true, nullable = false)
    val email: String,

    @Column(name = "student_id", unique = true, nullable = false)
    val studentId: Int = 20211234,

    @Column(name = "is_fee_paid", nullable = false, columnDefinition = "TINYINT(1)")
    var isFeePaid: Boolean = false,

    @Column(name = "name", nullable = false)
    val name: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    val id: Long? = null

    @Column(nullable = false)
    var department: Department = Department.SW
        protected set

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var role: Role = Role.USER
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

    fun updateDepartment(department: Department) {
        this.department = department
    }
}