package site.billilge.api.backend.domain.rental.reader

import org.springframework.stereotype.Component
import site.billilge.api.backend.domain.rental.entity.RentalStatusWorkerLog
import site.billilge.api.backend.domain.rental.repository.RentalStatusWorkerLogRepository

@Component
class RentalStatusWorkerLogReader(private val rentalStatusWorkerLogRepository: RentalStatusWorkerLogRepository) {

    fun readAllByRentalHistoryId(rentalHistoryId: Long): List<RentalStatusWorkerLog> =
        rentalStatusWorkerLogRepository.findAllByRentalHistoryIdOrderByCreatedAtAsc(rentalHistoryId)
}
