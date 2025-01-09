package site.billilge.api.backend.domain.item.enums

import com.fasterxml.jackson.annotation.JsonCreator

enum class ItemType(val displayName: String) {
    CONSUMPTION("소모품"),
    RENTAL("대여품");
}