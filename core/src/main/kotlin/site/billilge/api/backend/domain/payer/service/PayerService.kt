package site.billilge.api.backend.domain.payer.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.billilge.api.backend.common.utils.ExcelRow
import site.billilge.api.backend.core.port.ExcelGeneratorPort
import site.billilge.api.backend.core.vo.PageRequest
import site.billilge.api.backend.core.vo.PageResult
import site.billilge.api.backend.domain.member.appender.MemberAppender
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.member.reader.MemberReader
import site.billilge.api.backend.domain.payer.appender.PayerAppender
import site.billilge.api.backend.domain.payer.entity.Payer
import site.billilge.api.backend.domain.payer.reader.PayerReader
import site.billilge.api.backend.domain.payer.remover.PayerRemover
import java.io.ByteArrayInputStream
import java.time.Year

@Service
@Transactional(readOnly = true)
class PayerService(
    private val payerReader: PayerReader,
    private val payerAppender: PayerAppender,
    private val payerRemover: PayerRemover,
    private val memberReader: MemberReader,
    private val memberAppender: MemberAppender,
    private val excelGeneratorPort: ExcelGeneratorPort,
) {
    fun isPayer(name: String, studentId: String): Boolean {
        val enrollmentYear = studentId.substring(0, 4)
        val payers = payerReader.readAllByNameAndEnrollmentYear(name, enrollmentYear)
        if (payers.isEmpty()) return false
        if (payers.size == 1 && studentId.substring(4, 8) == "XXXX") return true
        return payers.any { it.studentId == studentId }
    }

    @Transactional
    fun updatePayerInfo(member: Member) {
        val payers = payerReader.readAllByNameAndEnrollmentYear(member.name, member.studentId.substring(0, 4))
        if (payers.isEmpty()) return

        val target = if (payers.size > 1) payers.first { it.studentId == member.studentId } else payers[0]
        payerAppender.save(target.copy(registered = true, studentId = member.studentId))
    }

    fun getAllPayers(pageRequest: PageRequest, search: String): PageResult<Payer> =
        payerReader.readAllByNameContaining(search, pageRequest)

    @Transactional
    fun addPayers(payers: List<Pair<String, String>>) {
        val newPayers = mutableListOf<Payer>()

        payers.forEach { (name, studentId) ->
            val enrollmentYear = studentId.substring(0, 4)
            val registeredMember = memberReader.readByStudentIdAndName(studentId, name)

            if (!isPayer(name, studentId)) {
                newPayers.add(
                    Payer(id = null, name = name, enrollmentYear = enrollmentYear,
                        studentId = studentId, registered = registeredMember != null)
                )
            }

            registeredMember?.let { memberAppender.save(it.copy(isFeePaid = true)) }
        }

        payerAppender.saveAll(newPayers)
    }

    @Transactional
    fun deletePayers(payerIds: List<Long>) {
        val studentIds = payerReader.readAllByIds(payerIds).mapNotNull { it.studentId }
        memberReader.readAllByStudentIds(studentIds)
            .map { it.copy(isFeePaid = false) }
            .let { memberAppender.saveAll(it) }
        payerRemover.removeAll(payerIds)
    }

    fun createPayerExcel(): ByteArrayInputStream {
        val startYear = 2015
        val currentYear = Year.now().value
        val headerTitles = arrayOf("이름", "학번")
        val sheetData = mutableMapOf<String, Pair<Array<String>, List<ExcelRow>>>()

        for (year in currentYear downTo startYear) {
            val yearText = "$year"
            val rows = payerReader.readAllByEnrollmentYear(yearText)
                .map { ExcelRow(it.name, it.studentId ?: "${yearText}XXXX") }
            sheetData[yearText] = headerTitles to rows
        }

        return excelGeneratorPort.generateByMultipleSheets(sheetData)
    }
}
