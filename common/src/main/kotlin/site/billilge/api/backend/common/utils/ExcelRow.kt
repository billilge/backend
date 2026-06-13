package site.billilge.api.backend.common.utils

data class ExcelRow(
    val data: List<String>
) {
    constructor(vararg data: String) : this(data.toList())
}
