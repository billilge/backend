package site.billilge.api.backend.domain.rental.remover

import org.springframework.stereotype.Component
import site.billilge.api.backend.domain.rental.repository.RentalRepository

@Component
class RentalRemover(private val rentalRepository: RentalRepository) {

    fun remove(id: Long) = rentalRepository.delete(id)
}
