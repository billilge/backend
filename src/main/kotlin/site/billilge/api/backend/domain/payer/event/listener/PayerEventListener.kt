package site.billilge.api.backend.domain.payer.event.listener

import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import site.billilge.api.backend.domain.payer.event.PayerAddEvent
import site.billilge.api.backend.domain.payer.event.PayerDeleteEvent

@Component
class PayerEventListener {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onPayerAdd(event: PayerAddEvent) {

    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onPayerDelete(event: PayerDeleteEvent) {

    }
}