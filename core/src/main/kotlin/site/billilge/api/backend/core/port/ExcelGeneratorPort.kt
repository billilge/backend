package site.billilge.api.backend.core.port

import site.billilge.api.backend.common.utils.ExcelRow
import java.io.ByteArrayInputStream

interface ExcelGeneratorPort {
    fun generateByMultipleSheets(
        sheetData: Map<String, Pair<Array<String>, List<ExcelRow>>>,
    ): ByteArrayInputStream
}
