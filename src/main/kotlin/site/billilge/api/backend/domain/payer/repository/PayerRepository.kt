package site.billilge.api.backend.domain.payer.repository

import org.springframework.data.jpa.repository.JpaRepository
import site.billilge.api.backend.domain.payer.entity.Payer

interface PayerRepository : JpaRepository<Payer, Long?> {
    fun findAllByNameAndEnrollmentYear(name: String, enrollmentYear: String): List<Payer>
}