package site.billilge.api.backend.domain.item.appender

import org.springframework.stereotype.Component
import site.billilge.api.backend.domain.item.entity.Item
import site.billilge.api.backend.domain.item.repository.ItemRepository

@Component
class ItemAppender(private val itemRepository: ItemRepository) {

    fun save(item: Item): Item = itemRepository.save(item)
}
