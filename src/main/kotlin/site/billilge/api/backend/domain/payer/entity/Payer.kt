package site.billilge.api.backend.domain.payer.entity

import jakarta.persistence.*

@Entity
@Table(
    name = "payer",
    indexes = [Index(name = "idx_payer_name_enrollment_year", columnList = "name, enrollment_year")]
)
class Payer(
    @Column(nullable = false)
    val name: String,

    @Column(name = "enrollment_year", nullable = false)
    val enrollmentYear: String,

    @Column(name = "student_id")
    var studentId: String? = null,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payer_id", nullable = false)
    var id: Long? = null

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    var registered: Boolean = false

    fun update(registered: Boolean, studentId: String) {
        this.registered = registered
        this.studentId = studentId
    }
}