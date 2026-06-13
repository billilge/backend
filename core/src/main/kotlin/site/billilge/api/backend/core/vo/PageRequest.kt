package site.billilge.api.backend.core.vo

data class PageRequest(
    val page: Int,
    val size: Int,
    val sortBy: String? = null,
    val ascending: Boolean = false,
)
