package site.billilge.api.backend.domain.payer.appender

import org.springframework.stereotype.Component
import site.billilge.api.backend.domain.payer.entity.Payer
import site.billilge.api.backend.domain.payer.repository.PayerRepository

@Component
class PayerAppender(private val payerRepository: PayerRepository) {

    fun save(payer: Payer): Payer = payerRepository.save(payer)

    fun saveAll(payers: List<Payer>): List<Payer> = payerRepository.saveAll(payers)
}
