package site.billilge.api.backend.domain.notification.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.notification.enums.NotificationType
import java.time.LocalDateTime

@Entity
@Table(name = "notifications")
@EntityListeners(AuditingEntityListener::class)
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

    @Column(name = "is_read", nullable = false, columnDefinition = "TINYINT(1)")
    var isRead: Boolean = false,

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    val id: Long? = null

    fun readNotification() {
        this.isRead = true
    }
}