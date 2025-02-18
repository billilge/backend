package site.billilge.api.backend.domain.rental.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.billilge.api.backend.domain.item.repository.ItemRepository
import site.billilge.api.backend.domain.member.repository.MemberRepository
import site.billilge.api.backend.domain.rental.dto.request.RentalRequest
import site.billilge.api.backend.domain.rental.dto.response.RentalHistoryDetail
import site.billilge.api.backend.domain.rental.entity.RentalHistory
import site.billilge.api.backend.domain.rental.enums.RentalStatus
import site.billilge.api.backend.domain.rental.exception.RentalErrorCode
import site.billilge.api.backend.domain.rental.repository.RentalRepository
import site.billilge.api.backend.global.exception.ApiException

@Service
@Transactional(readOnly = true)
class RentalService(
    private val memberRepository: MemberRepository,
    private val rentalRepository: RentalRepository,
    private val itemRepository: ItemRepository,
){
    @Transactional
    fun createRental(memberId: Long?, rentalRequest: RentalRequest){
        val item = itemRepository.findById(rentalRequest.itemId)
            .orElseThrow { ApiException(RentalErrorCode.ITEM_NOT_FOUND) }

        if(!rentalRequest.ignoreDuplicate) {
            val rentalHistory = rentalRepository.findByItemIdAndMemberIdAndRentalStatus(rentalRequest.itemId, memberId!!, RentalStatus.RENTAL)

            if(rentalHistory.isPresent)
                throw ApiException(RentalErrorCode.RENTAL_ITEM_DUPLICATED)
        }

        if(rentalRequest.count > item.count)
            throw ApiException(RentalErrorCode.ITEM_OUT_OF_STOCK)

        val rentUser = memberRepository.findById(memberId!!)
            .orElseThrow { ApiException(RentalErrorCode.MEMBER_NOT_FOUND) }

        val newRental = RentalHistory(
            member = rentUser,
            item = item,
            rentalStatus = RentalStatus.PENDING,
            rentAt =  rentalRequest.rentalDate
        )

        rentalRepository.save(newRental)
    }

    fun getMemberRentalHistory(memberId: Long?, rentalStatus: RentalStatus?): List<RentalHistoryDetail>{
        val rentalHistories = if (rentalStatus == null) {
            rentalRepository.findByMemberId(memberId!!)
        } else {
            rentalRepository.findByMemberIdAndRentalStatus(memberId!!, rentalStatus)
        }
        return rentalHistories.map { rentalHistory -> RentalHistoryDetail.from(rentalHistory) }
    }
}