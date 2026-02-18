package site.billilge.api.backend.domain.display.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "display_poster")
@EntityListeners(AuditingEntityListener::class)
class DisplayPoster(
    @Column(nullable = false)
    var title: String,

    @Column(name = "image_url", nullable = false)
    var imageUrl: String,

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1)")
    var isActive: Boolean = true,

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun update(title: String, imageUrl: String?) {
        this.title = title
        imageUrl?.let { this.imageUrl = it }
    }

    fun activate() {
        this.isActive = true
    }

    fun deactivate() {
        this.isActive = false
    }
}
