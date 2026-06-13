package site.billilge.api.backend.domain.payer.reader

import org.springframework.stereotype.Component
import site.billilge.api.backend.core.vo.PageRequest
import site.billilge.api.backend.core.vo.PageResult
import site.billilge.api.backend.domain.payer.entity.Payer
import site.billilge.api.backend.domain.payer.repository.PayerRepository

@Component
class PayerReader(private val payerRepository: PayerRepository) {

    fun readAllByNameAndEnrollmentYear(name: String, enrollmentYear: String): List<Payer> =
        payerRepository.findAllByNameAndEnrollmentYear(name, enrollmentYear)

    fun readAllByIds(ids: List<Long>): List<Payer> = payerRepository.findAllByIds(ids)

    fun readAllByNameContaining(name: String, pageRequest: PageRequest): PageResult<Payer> =
        payerRepository.findAllByNameContaining(name, pageRequest)

    fun readAllByEnrollmentYear(enrollmentYear: String): List<Payer> =
        payerRepository.findAllByEnrollmentYear(enrollmentYear)

    fun readAll(): List<Payer> = payerRepository.findAll()
}
