package site.billilge.api.backend.domain.payer.entity

import jakarta.persistence.*

@Entity
@Table(name = "payer")
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