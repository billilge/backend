package site.billilge.api.backend.domain.notification.entity

import jakarta.persistence.*
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.notification.enums.NotificationType
import java.time.Instant

@Entity
@Table(name = "notifications")
class Notification(
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne
    val member: Member,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    val type: NotificationType,

    @Column(name = "message", nullable = false)
    val message: String,

    @Column(name = "link", nullable = false)
    val link: String,

    @Column(name = "is_read", nullable = false)
    var isRead: Boolean = false,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    val id: Long? = null
}