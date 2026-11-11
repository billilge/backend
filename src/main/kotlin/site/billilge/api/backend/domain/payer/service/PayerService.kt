package site.billilge.api.backend.domain.payer.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.member.repository.MemberRepository
import site.billilge.api.backend.domain.payer.entity.Payer
import site.billilge.api.backend.domain.payer.repository.PayerRepository
import site.billilge.api.backend.global.dto.PageableCondition
import site.billilge.api.backend.global.dto.SearchCondition
import site.billilge.api.backend.global.utils.ExcelGenerator
import site.billilge.api.backend.global.utils.ExcelRow
import java.io.ByteArrayInputStream
import java.time.Year

@Service
@Transactional(readOnly = true)
class PayerService(
    private val payerRepository: PayerRepository,

    private val memberRepository: MemberRepository,

    private val excelGenerator: ExcelGenerator
) {
    fun isPayer(name: String, studentId: String): Boolean {
        val enrollmentYear = studentId.substring(0, 4)

        val payerResults = payerRepository.findAllByNameAndEnrollmentYear(name, enrollmentYear)

        if (payerResults.isEmpty()) return false

        if (payerResults.size == 1) {
            val studentIdLastNumber = studentId.substring(4, 8)

            if (studentIdLastNumber == "XXXX") return true
        }

        return payerResults.any { it.studentId == studentId }
    }

    @Transactional
    fun updatePayerInfo(member: Member) {
        val memberName = member.name
        val studentId = member.studentId
        val enrollmentYear = studentId.substring(0, 4)

        val payerResults = payerRepository.findAllByNameAndEnrollmentYear(memberName, enrollmentYear)

        if (payerResults.isEmpty()) return

        if (payerResults.size > 1) {
            val payer = payerResults
                .first { it.studentId == studentId }

            payer.update(true, studentId)
        }

        payerResults[0].update(true, studentId)
    }

    fun getAllPayers(pageableCondition: PageableCondition, searchCondition: SearchCondition): Page<Payer> {
        val pageRequest = PageRequest.of(
            pageableCondition.pageNo,
            pageableCondition.size,
            resolveSort(pageableCondition.criteria)
        )
        val search = searchCondition.search

        // 검색어가 없으면 LIKE '%%'는 한 건도 걸러내지 못하면서 선행 와일드카드 때문에
        // 인덱스 접근만 막는다. 목록 첫 진입이 이 경로이므로 술어 자체를 빼서
        // 정렬 인덱스를 타고 LIMIT에서 끊게 한다.
        if (search.isBlank()) {
            return payerRepository.findAll(pageRequest)
        }

        return payerRepository.findAllByNameContaining(search, pageRequest)
    }

    /**
     * 납부자 목록의 정렬 기준을 만드는 유일한 지점.
     *
     * 기본 정렬은 `idx_payer_enrollment_year_name(enrollment_year DESC, name)` 의 순서와 맞춘다.
     * InnoDB 보조 인덱스는 뒤에 PK가 붙으므로 `id ASC` 까지 인덱스 순서로 읽을 수 있고,
     * 정렬 키가 인덱스와 어긋나면 조건에 걸린 전체 행을 filesort 하게 된다.
     *
     * criteria는 클라이언트가 넘기는 값이라 화이트리스트로 받는다. 선언형 @Query에는
     * 프로퍼티명이 검증 없이 그대로 붙어서, 없는 이름이 들어오면 쿼리 자체가 깨진다.
     */
    private fun resolveSort(criteria: String?): Sort = when (criteria) {
        "name" -> Sort.by(
            Sort.Order.asc("name"),
            Sort.Order.desc("enrollmentYear"),
            Sort.Order.asc("id")
        )

        else -> Sort.by(
            Sort.Order.desc("enrollmentYear"),
            Sort.Order.asc("name"),
            Sort.Order.asc("id")
        )
    }

    @Transactional
    fun addPayers(payers: List<Pair<String, String>>) {
        val newPayers = mutableListOf<Payer>()
        payers.forEach { (name, studentId) ->
            val enrollmentYear = studentId.substring(0, 4)
            val registeredMember = memberRepository.findByStudentIdAndName(studentId, name)
            val registered = registeredMember != null

            if (!isPayer(name, studentId)) {
                val payer = Payer(
                    name = name,
                    enrollmentYear = enrollmentYear,
                    studentId = studentId
                ).apply {
                    this.registered = registered
                }

                newPayers.add(payer)
            }

            registeredMember?.isFeePaid = true
        }

        payerRepository.saveAll(newPayers)
    }

    @Transactional
    fun deletePayers(payerIds: List<Long>) {
        val payerStudentIds = payerRepository.findAllByIds(payerIds)
            .mapNotNull { it.studentId }

        memberRepository.findAllByStudentIds(payerStudentIds)
            .forEach { member ->
                member.isFeePaid = false
            }

        payerRepository.deleteAllById(payerIds)
    }

    fun createPayerExcel(): ByteArrayInputStream {
        val startYear = 2015
        val currentYear = Year.now().value
        val headerTitles = arrayOf("이름", "학번")
        val sheetData = mutableMapOf<String, Pair<Array<String>, List<ExcelRow>>>()

        for (year in currentYear downTo startYear) {
            val yearText = "$year"
            val payersByYearExcelRow = payerRepository.findAllByEnrollmentYear(yearText)
                .map { payer -> ExcelRow(payer.name, payer.studentId ?: "${yearText}XXXX") }

            sheetData.put(yearText, headerTitles to payersByYearExcelRow)
        }

        return excelGenerator.generateByMultipleSheets(sheetData)
    }
}
