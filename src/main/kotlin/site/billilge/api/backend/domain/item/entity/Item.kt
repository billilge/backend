package site.billilge.api.backend.domain.item.entity

import jakarta.persistence.*
import site.billilge.api.backend.domain.item.enums.ItemType

@Entity
@Table(name = "item")
class Item(
    @Column(nullable = false)
    val name: String,
    @Column(nullable = false)
    val type: ItemType,
    @Column(nullable = false)
    val count: Int,
    @Column(name = "image_url", nullable = true)
    val imageUrl: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null
}