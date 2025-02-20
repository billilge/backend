package site.billilge.api.backend.domain.item.entity

import jakarta.persistence.*
import site.billilge.api.backend.domain.item.enums.ItemType

@Entity
@Table(name = "item")
class Item(
    @Column(nullable = false)
    var name: String,
    @Column(nullable = false)
    var type: ItemType,
    @Column(nullable = false)
    var count: Int,
    @Column(name = "image_url", nullable = true)
    var imageUrl: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun update(name: String, type: ItemType, count: Int, imageUrl: String?) {
        this.name = name
        this.type = type
        this.count = count
        imageUrl?.let { this.imageUrl = it }
    }
}