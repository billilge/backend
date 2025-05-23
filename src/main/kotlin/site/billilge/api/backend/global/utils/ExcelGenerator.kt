package site.billilge.api.backend.global.utils

import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.streaming.SXSSFSheet
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

private const val HEADER_ROW = 0

@Component
class ExcelGenerator {

    fun generateByMultipleSheets(
        sheetData: Map<String, Pair<Array<String>, List<ExcelRow>>>
    ): ByteArrayInputStream {
        val workbook = SXSSFWorkbook()

        try {
            sheetData.forEach { (sheetName, sheetContent) ->
                val (headerTitles, rows) = sheetContent
                val sheet = workbook.createSheet(sheetName)
                styleHeaders(workbook, sheet, headerTitles)
                fillData(sheet, rows)
            }

            val out = ByteArrayOutputStream()
            workbook.write(out)
            return ByteArrayInputStream(out.toByteArray())
        } finally {
            workbook.dispose()
            workbook.close()
        }
    }

    private fun styleHeaders(workbook: SXSSFWorkbook, sheet: SXSSFSheet, headerTitles: Array<String>) {
        val headerFont = workbook.createFont()
        headerFont.bold = true

        val headerCellStyle = workbook.createCellStyle()
        headerCellStyle.setFont(headerFont)
        headerCellStyle.fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
        headerCellStyle.fillPattern = org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND

        val headerRow = sheet.createRow(HEADER_ROW)
        headerTitles.forEachIndexed { col, title ->
            val cell = headerRow.createCell(col)
            cell.setCellValue(title)
            cell.cellStyle = headerCellStyle
        }

        //이름 칸 너비
        sheet.setColumnWidth(0, 10 * 256)
        //학번 칸 너비
        sheet.setColumnWidth(1, 16 * 256)
    }

    private fun fillData(sheet: SXSSFSheet, rows: List<ExcelRow>) {
        rows.forEachIndexed { index, excelRow ->
            val row = sheet.createRow(index + 1)
            excelRow.data.forEachIndexed { propertyIndex, property ->
                row.createCell(propertyIndex).setCellValue(property)
            }
        }
    }
}