package site.billilge.api.backend.domain.payer.remover

import org.springframework.stereotype.Component
import site.billilge.api.backend.domain.payer.repository.PayerRepository

@Component
class PayerRemover(private val payerRepository: PayerRepository) {

    fun removeAll(ids: List<Long>) = payerRepository.deleteAll(ids)
}
