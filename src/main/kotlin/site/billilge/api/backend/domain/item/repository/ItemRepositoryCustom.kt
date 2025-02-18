package site.billilge.api.backend.domain.item.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import site.billilge.api.backend.domain.item.dto.response.AdminItemDetail

interface ItemRepositoryCustom {
    fun findAllAsAdminItemDetailByKeyword(keyword: String, pageable: Pageable): Page<AdminItemDetail>
}