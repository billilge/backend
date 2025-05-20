package site.billilge.api.backend.domain.payer.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.member.repository.MemberRepository
import site.billilge.api.backend.domain.payer.dto.request.PayerDeleteRequest
import site.billilge.api.backend.domain.payer.dto.request.PayerRequest
import site.billilge.api.backend.domain.payer.dto.response.PayerExcelFileResponse
import site.billilge.api.backend.domain.payer.dto.response.PayerFindAllResponse
import site.billilge.api.backend.domain.payer.dto.response.PayerSummary
import site.billilge.api.backend.domain.payer.entity.Payer
import site.billilge.api.backend.domain.payer.event.PayerAddEvent
import site.billilge.api.backend.domain.payer.event.PayerDeleteEvent
import site.billilge.api.backend.domain.payer.repository.PayerRepository
import site.billilge.api.backend.global.dto.PageableCondition
import site.billilge.api.backend.global.dto.SearchCondition
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class PayerService(
    private val publisher: ApplicationEventPublisher,
    private val payerRepository: PayerRepository,
    private val memberRepository: MemberRepository,
    @Value("\${cloud.aws.s3.base-url}")
    private val s3BaseUrl: String,
) {
    fun isPayer(name: String, studentId: String): Boolean {
        val enrollmentYear = studentId.substring(0, 4)

        val payerResults = payerRepository.findAllByNameAndEnrollmentYear(name, enrollmentYear)

        if (payerResults.isEmpty()) return false

        if (payerResults.size > 1) {
            return payerResults.any { it.studentId == studentId }
        }

        return true
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

    fun getAllPayers(pageableCondition: PageableCondition, searchCondition: SearchCondition): PayerFindAllResponse {
        val pageRequest = PageRequest.of(
            pageableCondition.pageNo,
            pageableCondition.size,
            Sort.by(Sort.Direction.DESC, pageableCondition.criteria ?: "enrollmentYear")
        )
        val payers = payerRepository.findAllByNameContaining(searchCondition.search, pageRequest)
        val payerSummaries = payers
            .map { PayerSummary.from(it) }
            .toList()

        return PayerFindAllResponse(payerSummaries, payers.totalPages)
    }

    @Transactional
    fun addPayers(request: PayerRequest) {
        val newPayers = mutableListOf<Payer>()
        request.payers.forEach { payerItem ->
            val name = payerItem.name
            val studentId = payerItem.studentId
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

        publisher.publishEvent(PayerAddEvent(newPayers.map { it.id }))
    }

    @Transactional
    fun deletePayers(request: PayerDeleteRequest) {
        val payerStudentIds = payerRepository.findAllByIds(request.payerIds)
            .mapNotNull { it.studentId }
            .toList()

        memberRepository.findAllByStudentIds(payerStudentIds)
            .forEach { member ->
                member.isFeePaid = false
            }

        payerRepository.deleteAllById(request.payerIds)

        publisher.publishEvent(PayerDeleteEvent(request.payerIds))
    }

    fun getExcelFileUrl(): PayerExcelFileResponse {
        val currentYear = LocalDate.now().year
        return PayerExcelFileResponse(s3BaseUrl + "/payers/payer_${currentYear}.xlsx")
    }
}