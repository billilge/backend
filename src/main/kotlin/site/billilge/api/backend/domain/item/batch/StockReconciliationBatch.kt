package site.billilge.api.backend.domain.item.batch

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import site.billilge.api.backend.domain.item.service.ItemService
import site.billilge.api.backend.global.logging.log

@Component
class StockReconciliationBatch(
    private val itemService: ItemService,
) {
    @Scheduled(cron = "0 0 3 * * *")
    fun reconcile() {
        log.info { "Stock reconciliation started." }
        val fixedCount = itemService.reconcileStocks()
        if (fixedCount == 0) {
            log.info { "Stock reconciliation completed. No inconsistencies found." }
        } else {
            log.warn { "Stock reconciliation completed. Fixed $fixedCount item(s)." }
        }
    }
}
