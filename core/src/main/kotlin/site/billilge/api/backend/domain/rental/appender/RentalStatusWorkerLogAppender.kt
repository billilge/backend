package site.billilge.api.backend.domain.rental.appender

import org.springframework.stereotype.Component
import site.billilge.api.backend.domain.rental.entity.RentalStatusWorkerLog
import site.billilge.api.backend.domain.rental.repository.RentalStatusWorkerLogRepository

@Component
class RentalStatusWorkerLogAppender(private val rentalStatusWorkerLogRepository: RentalStatusWorkerLogRepository) {

    fun save(log: RentalStatusWorkerLog): RentalStatusWorkerLog =
        rentalStatusWorkerLogRepository.save(log)
}
