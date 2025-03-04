package site.billilge.api.backend.domain.payer.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import site.billilge.api.backend.domain.payer.entity.Payer

interface PayerRepository : JpaRepository<Payer, Long?> {
    fun findAllByNameAndEnrollmentYear(name: String, enrollmentYear: String): List<Payer>

    @Query("select p from Payer p where p.id in :ids")
    fun findAllByIds(@Param("ids") ids: List<Long>): List<Payer>

    @Query("SELECT p FROM Payer p WHERE p.name LIKE CONCAT('%', :name, '%')")
    fun findAllByNameContaining(@Param("name") name: String, pageable: Pageable): Page<Payer>
}