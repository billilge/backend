package site.billilge.api.backend.domain.payer.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import site.billilge.api.backend.domain.payer.entity.Payer

interface PayerRepository {
    fun findById(id: Long): Payer?
    fun findAll(): List<Payer>
    fun save(payer: Payer): Payer
    fun saveAll(payers: List<Payer>): List<Payer>
    fun delete(id: Long)
    fun deleteAll(ids: List<Long>)
    fun findAllByNameAndEnrollmentYear(name: String, enrollmentYear: String): List<Payer>
    fun findAllByIds(ids: List<Long>): List<Payer>
    fun findAllByNameContaining(name: String, pageable: Pageable): Page<Payer>
    fun findAllByEnrollmentYear(enrollmentYear: String): List<Payer>
}
