package site.billilge.api.backend.domain.rental.entity

import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.rental.enums.RentalStatus
import java.time.LocalDateTime

@Entity
@Table(name = "rental_status_worker_log")
class RentalStatusWorkerLog(
    @JoinColumn(name = "rental_history_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    val rentalHistory: RentalHistory,

    @Column(name = "rental_status", nullable = false)
    @Enumerated(EnumType.STRING)
    val rentalStatus: RentalStatus,

    @JoinColumn(name = "worker_id", nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    val worker: Member? = null,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_status_worker_log_id", nullable = false)
    val id: Long? = null

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
}
