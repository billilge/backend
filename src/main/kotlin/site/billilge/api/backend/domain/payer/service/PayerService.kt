package site.billilge.api.backend.domain.payer.service

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.member.repository.MemberRepository
import site.billilge.api.backend.domain.payer.dto.request.PayerDeleteRequest
import site.billilge.api.backend.domain.payer.dto.request.PayerRequest
import site.billilge.api.backend.domain.payer.dto.response.PayerFindAllResponse
import site.billilge.api.backend.domain.payer.dto.response.PayerSummary
import site.billilge.api.backend.domain.payer.entity.Payer
import site.billilge.api.backend.domain.payer.repository.PayerRepository
import site.billilge.api.backend.global.dto.PageableCondition
import site.billilge.api.backend.global.dto.SearchCondition

@Service
@Transactional(readOnly = true)
class PayerService(
    private val payerRepository: PayerRepository,
    private val memberRepository: MemberRepository
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

                payerRepository.save(payer)
            }

            registeredMember?.isFeePaid = true
        }
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
    }
}