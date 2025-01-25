package site.billilge.api.backend.domain.rental.entity

import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import site.billilge.api.backend.domain.item.entity.Item
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.rental.enums.RentalStatus
import java.time.Instant

@Entity
@Table(name = "rental_history")
@EntityListeners(AuditingEntityListener::class)
class RentalHistory(
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne
    val member: Member,

    @JoinColumn(name = "item_id", nullable = false)
    @ManyToOne
    val item: Item,

    @Column(name = "rental_status", nullable = false)
    @Enumerated(EnumType.STRING)
    val rentalStatus: RentalStatus,

    @JoinColumn(name = "worker_id", nullable =  true)
    @ManyToOne
    val worker: Member? = null,

    @Column(name = "rent_at", nullable = false)
    val rentAt: Instant
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id", nullable = false)
    val id: Long? = null
}