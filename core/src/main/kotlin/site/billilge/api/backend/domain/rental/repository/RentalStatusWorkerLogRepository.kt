package site.billilge.api.backend.domain.rental.repository

import site.billilge.api.backend.domain.rental.entity.RentalStatusWorkerLog

interface RentalStatusWorkerLogRepository {
    fun save(log: RentalStatusWorkerLog): RentalStatusWorkerLog
    fun findAllByRentalHistoryIdOrderByCreatedAtAsc(rentalHistoryId: Long): List<RentalStatusWorkerLog>
}
