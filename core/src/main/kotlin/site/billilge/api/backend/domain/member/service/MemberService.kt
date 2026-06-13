package site.billilge.api.backend.domain.member.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.billilge.api.backend.common.exception.ApiException
import site.billilge.api.backend.core.vo.PageRequest
import site.billilge.api.backend.core.vo.PageResult
import site.billilge.api.backend.domain.configvalue.enums.ConfigValueKeys
import site.billilge.api.backend.domain.configvalue.service.ConfigValueService
import site.billilge.api.backend.domain.member.appender.MemberAppender
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.member.enums.Role
import site.billilge.api.backend.domain.member.exception.MemberErrorCode
import site.billilge.api.backend.domain.member.reader.MemberReader
import site.billilge.api.backend.domain.payer.service.PayerService

@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberReader: MemberReader,
    private val memberAppender: MemberAppender,
    private val payerService: PayerService,
    private val configValueService: ConfigValueService,
) {
    @Transactional
    fun signUp(email: String, studentId: String, name: String): Member {
        if (memberReader.existsByEmail(email)) throw ApiException(MemberErrorCode.EMAIL_ALREADY_EXISTS)

        if (memberReader.existsByStudentIdAndName(studentId, name)) {
            val existing = memberReader.readByStudentId(studentId)!!
            return memberAppender.save(existing.copy(email = email))
        }

        val isFeePaid = payerService.isPayer(name, studentId)
        val newMember = Member(id = null, name = name, studentId = studentId, isFeePaid = isFeePaid, email = email)
        val saved = memberAppender.save(newMember)
        payerService.updatePayerInfo(saved)
        return saved
    }

    fun loginAdmin(studentId: String, password: String): Member {
        val member = memberReader.readByStudentId(studentId)
            ?: throw ApiException(MemberErrorCode.MEMBER_NOT_FOUND)

        if (password != configValueService.getValueByKey(ConfigValueKeys.ADMIN_PASSWORD.key))
            throw ApiException(MemberErrorCode.ADMIN_PASSWORD_MISMATCH)

        if (member.role !in listOf(Role.ADMIN, Role.GA, Role.WORKER))
            throw ApiException(MemberErrorCode.FORBIDDEN)

        return member
    }

    fun findById(memberId: Long): Member = memberReader.read(memberId)

    fun findAllWorkers(): List<Member> = memberReader.readAllByRoleIn(setOf(Role.WORKER, Role.ADMIN, Role.GA))

    fun getAdminList(pageRequest: PageRequest, search: String): PageResult<Member> =
        memberReader.readAllByRoleInAndNameContaining(listOf(Role.ADMIN, Role.WORKER, Role.GA), search, pageRequest)

    fun getAllMembers(pageRequest: PageRequest, search: String): PageResult<Member> =
        memberReader.readAllByNameContaining(search, pageRequest)

    @Transactional
    fun updateAdminRole(memberId: Long, role: Role) {
        val member = memberReader.read(memberId)
        memberAppender.save(member.copy(role = role))
    }

    @Transactional
    fun addAdmins(memberIds: List<Long>, role: Role) {
        memberReader.readAllByIds(memberIds)
            .map { it.copy(role = role) }
            .let { memberAppender.saveAll(it) }
    }

    @Transactional
    fun deleteAdmins(memberIds: List<Long>) {
        memberReader.readAllByIds(memberIds)
            .map { it.copy(role = Role.USER) }
            .let { memberAppender.saveAll(it) }
    }

    @Transactional
    fun setMemberFCMToken(memberId: Long, token: String) {
        val member = memberReader.read(memberId)
        memberAppender.save(member.copy(fcmToken = token))
    }

    @Transactional
    fun clearFcmToken(memberId: Long) {
        val member = memberReader.readOrNull(memberId) ?: return
        memberAppender.save(member.copy(fcmToken = null))
    }
}
