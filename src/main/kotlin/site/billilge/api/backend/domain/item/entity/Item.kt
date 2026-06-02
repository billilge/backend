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

    @Column(name = "total_count", nullable = false)
    var totalCount: Int = count
        protected set

    fun update(name: String, type: ItemType, count: Int, imageUrl: String?) {
        val delta = count - this.totalCount
        this.name = name
        this.type = type
        this.totalCount = count
        this.count = maxOf(0, this.count + delta)
        imageUrl?.let { this.imageUrl = it }
    }

    fun fixCount(correctedCount: Int) {
        this.count = maxOf(0, correctedCount)
    }

    fun addCount(count: Int) {
        this.count += count
    }

    fun subtractCount(count: Int) {
        if (count > this.count) {
            this.count = 0
            return
        }
        this.count -= count
    }
}