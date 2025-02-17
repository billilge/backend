package site.billilge.api.backend.global.dto

import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import org.springdoc.core.annotations.ParameterObject

@ParameterObject
data class SearchCondition(
    @Parameter(
        name = "search",
        required = false,
        allowEmptyValue = true,
        example = "",
        schema = Schema(description = "검색어", defaultValue = "")
    )
    val search: String = ""
)
