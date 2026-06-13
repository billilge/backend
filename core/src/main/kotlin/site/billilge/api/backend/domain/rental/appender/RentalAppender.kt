package site.billilge.api.backend.domain.rental.appender

import org.springframework.stereotype.Component
import site.billilge.api.backend.domain.rental.entity.RentalHistory
import site.billilge.api.backend.domain.rental.repository.RentalRepository

@Component
class RentalAppender(private val rentalRepository: RentalRepository) {

    fun save(rentalHistory: RentalHistory): RentalHistory = rentalRepository.save(rentalHistory)
}
