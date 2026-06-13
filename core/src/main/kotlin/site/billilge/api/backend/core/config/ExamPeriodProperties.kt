package site.billilge.api.backend.core.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.LocalDate

@ConfigurationProperties(prefix = "exam-period")
data class ExamPeriodProperties(
    val startDate: LocalDate = LocalDate.of(2000, 1, 1),
    val endDate: LocalDate = LocalDate.of(2000, 1, 1),
)
