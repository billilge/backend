package site.billilge.api.backend.domain.display.dto.response

data class DisplayResponse(
    val posters: List<PosterDetail>,
    val schedules: List<Map<String, List<String>>>,
    val items: List<DisplayItemDetail>,
) {
    data class PosterDetail(
        val posterId: Long,
        val title: String,
        val imageUrl: String,
    )

    data class DisplayItemDetail(
        val itemName: String,
        val count: Int,
        val imageUrl: String,
    )
}
