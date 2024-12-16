package site.billilge.api.backend.domain.work.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import site.billilge.api.backend.domain.member.entity.Member
import java.time.LocalDateTime

@Entity
@Table(name = "work_history")
class WorkHistory(
    @JoinColumn(name = "worker_id", nullable = false)
    @ManyToOne
    val worker: Member,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_history_id")
    val id: Long? = null

    @CreatedDate
    @Column(name = "start_time", nullable = false)
    var startTime: LocalDateTime = LocalDateTime.now()

    @Column(name = "end_time", nullable = true)
    var endTime: LocalDateTime? = null
}