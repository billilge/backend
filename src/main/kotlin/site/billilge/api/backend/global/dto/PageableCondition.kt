package site.billilge.api.backend.global.dto

import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import org.springdoc.core.annotations.ParameterObject

@ParameterObject
data class PageableCondition(
    @Parameter(
        name = "pageNo",
        required = false,
        example = "0",
        schema = Schema(description = "페이지 번호", defaultValue = "0")
    )
    val pageNo: Int = 0,
    @Parameter(
        name = "size",
        required = false,
        example = "10",
        schema = Schema(description = "페이지당 개수", defaultValue = "10")
    )
    val size: Int = 10,
    @Parameter(
        name = "criteria",
        required = false,
        allowEmptyValue = true,
        example = "",
        schema = Schema(description = "조회 기준", defaultValue = "")
    )
    val criteria: String? = null
)