package site.billilge.api.backend.domain.rental.entity

import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import site.billilge.api.backend.domain.item.entity.Item
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.rental.enums.RentalStatus
import java.time.LocalDateTime

@Entity
@Table(name = "rental_history")
@EntityListeners(AuditingEntityListener::class)
class RentalHistory(
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne
    val member: Member,

    @JoinColumn(name = "item_id", nullable = false)
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    val item: Item,

    @Column(name = "rental_status", nullable = false)
    @Enumerated(EnumType.STRING)
    var rentalStatus: RentalStatus,

    @Column(name = "rent_at", nullable = false)
    val rentAt: LocalDateTime,

    @Column(name = "returned_at", nullable = true)
    var returnedAt: LocalDateTime? = null,

    @Column(name = "rented_count", nullable = false)
    val rentedCount: Int,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id", nullable = false)
    val id: Long? = null

    @JoinColumn(name = "worker_id", nullable =  true)
    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    var worker: Member? = null
        protected set

    @CreatedDate
    @Column(name = "applicated_at", nullable = false)
    var applicatedAt: LocalDateTime = LocalDateTime.now()
        protected set


    fun updateStatus(newStatus: RentalStatus) {
        if (newStatus == RentalStatus.RETURNED) {
            returnedAt = LocalDateTime.now()
        }

        this.rentalStatus = newStatus
    }

    fun updateWorker(worker: Member) {
        this.worker = worker
    }
}