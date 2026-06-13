package site.billilge.api.backend.domain.item.remover

import org.springframework.stereotype.Component
import site.billilge.api.backend.domain.item.repository.ItemRepository

@Component
class ItemRemover(private val itemRepository: ItemRepository) {

    fun remove(id: Long) = itemRepository.delete(id)
}
