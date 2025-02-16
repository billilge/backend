package site.billilge.api.backend.domain.admin.service

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import site.billilge.api.backend.domain.admin.dto.response.AdminFindAllResponse
import site.billilge.api.backend.domain.admin.dto.response.AdminMemberDetail
import site.billilge.api.backend.domain.admin.exception.AdminErrorCode
import site.billilge.api.backend.domain.item.dto.request.ItemRequest
import site.billilge.api.backend.domain.item.dto.response.ItemDetail
import site.billilge.api.backend.domain.item.dto.response.ItemFindAllResponse
import site.billilge.api.backend.domain.item.entity.Item
import site.billilge.api.backend.domain.item.enums.ItemType
import site.billilge.api.backend.domain.item.repository.ItemRepository
import site.billilge.api.backend.domain.member.enums.Role
import site.billilge.api.backend.domain.member.exception.MemberErrorCode
import site.billilge.api.backend.domain.member.repository.MemberRepository
import site.billilge.api.backend.domain.payer.dto.request.PayerRequest
import site.billilge.api.backend.domain.payer.dto.response.PayerFindAllResponse
import site.billilge.api.backend.domain.payer.dto.response.PayerSummary
import site.billilge.api.backend.domain.payer.entity.Payer
import site.billilge.api.backend.domain.payer.repository.PayerRepository
import site.billilge.api.backend.domain.payer.service.PayerService
import site.billilge.api.backend.global.exception.ApiException
import site.billilge.api.backend.global.exception.GlobalErrorCode
import site.billilge.api.backend.global.external.s3.S3Service

@Service
@Transactional(readOnly = true)
class AdminService(
    private val memberRepository: MemberRepository,
    private val itemRepository: ItemRepository,
    private val payerRepository: PayerRepository,
    private val s3Service: S3Service,
    private val payerService: PayerService,
) {
    fun getAllItems(): ItemFindAllResponse {
        val itemDetails = itemRepository.findAll()
            .map { item -> ItemDetail.from(item) }

        return ItemFindAllResponse(itemDetails)
    }

    @Transactional
    fun addItem(image: MultipartFile, itemRequest: ItemRequest) {
        if (itemRepository.existsByName(itemRequest.name))
            throw ApiException(AdminErrorCode.ITEM_NAME_ALREADY_EXISTS)

        val imageUrl = s3Service.uploadImageFile(image)
            ?: throw ApiException(GlobalErrorCode.IMAGE_UPLOAD_FAILED)

        val type = if (itemRequest.isConsumption) ItemType.CONSUMPTION else ItemType.RENTAL

        val newItem = Item(
            name = itemRequest.name,
            type = type,
            count = itemRequest.count,
            imageUrl = imageUrl,
        )

        itemRepository.save(newItem)
    }

    @Transactional
    fun updateItem(image: MultipartFile?, itemId: Long, itemRequest: ItemRequest) {
        val item = itemRepository.findById(itemId)
            .orElseThrow { ApiException(AdminErrorCode.ITEM_NOT_FOUND) }
        val imageUrl = image?.let { s3Service.uploadImageFile(it) }
            ?: item.imageUrl
        val type = if (itemRequest.isConsumption) ItemType.CONSUMPTION else ItemType.RENTAL

        item.update(
            name = itemRequest.name,
            type = type,
            count = itemRequest.count,
            imageUrl = imageUrl,
        )
    }

    @Transactional
    fun deleteItem(itemId: Long): Boolean {
        val item = itemRepository.findById(itemId)
            .orElseThrow { ApiException(AdminErrorCode.ITEM_NOT_FOUND) }

        val imageUrl = item.imageUrl
        var isEntityDeleted: Boolean

        try {
            itemRepository.deleteById(itemId)
            isEntityDeleted = true
        } catch (e: Exception) {
            e.printStackTrace()
            isEntityDeleted = false
        }

        if (isEntityDeleted) {
            s3Service.deleteImageFile(imageUrl)
            return true
        }

        return false
    }

    //TODO: QueryDSL 도입할 때 필터링 부분 비즈니스 로직에서 제외하기
    fun getAdminList(): AdminFindAllResponse {
        val adminDetails = memberRepository.findAll()
            .filter { it.role == Role.ADMIN }
            .map { AdminMemberDetail.from(it) }
            .toList()

        return AdminFindAllResponse(adminDetails)
    }

    @Transactional
    fun updateMemberRole(memberId: Long) {
        val member = memberRepository.findById(memberId)
            .orElseThrow { ApiException(MemberErrorCode.MEMBER_NOT_FOUND) }

        val newRole = if (member.role == Role.ADMIN) Role.USER else Role.ADMIN

        member.updateRole(newRole)
    }

    fun getAllPayers(pageNo: Int, size: Int, criteria: String): PayerFindAllResponse {
        val pageRequest = PageRequest.of(pageNo, size, Sort.by(Sort.Direction.DESC, criteria))
        val payers = payerRepository.findAll(pageRequest)
            .map { PayerSummary.from(it) }
            .toList()

        return PayerFindAllResponse(payers)
    }

    @Transactional
    fun addPayers(request: PayerRequest) {
        request.payers.forEach { payerItem ->
            val name = payerItem.name
            val studentId = payerItem.studentId
            val enrollmentYear = studentId.substring(0, 4)
            val registered = memberRepository.existsByStudentIdAndName(studentId, name)

            if (!payerService.isPayer(name, studentId)) {
                val payer = Payer(
                    name = name,
                    enrollmentYear = enrollmentYear,
                    studentId = studentId
                ).apply {
                    this.registered = registered
                }

                payerRepository.save(payer)
            }
        }
    }
}