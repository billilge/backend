package site.billilge.api.backend.domain.payer.dto.response

import io.swagger.v3.oas.annotations.media.Schema

data class PayerExcelFileResponse(
    @field:Schema(description = "S3에 업로드된 납부자 목록 엑셀 파일 URL")
    val fileUrl: String
)
