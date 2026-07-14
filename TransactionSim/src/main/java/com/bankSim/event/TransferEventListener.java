package com.bankSim.event;

import com.bankSim.model.Analytics;
import com.bankSim.model.Transfer;
import com.bankSim.repos.AnalyticsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class TransferEventListener {

    private static final Logger logger = LoggerFactory.getLogger(TransferEventListener.class);
    private final AnalyticsRepository analyticsRepository;

    public TransferEventListener(AnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }

    @Async
    @EventListener
    public void handleTransferCompletedLog(TransferCompletedEvent event) {
        Transfer transfer = event.getTransfer();
        logger.info("📢 [EVENT LOG] Transfer Completed: ID={}, FromAccount={}, ToAccount={}, Amount={}, Status={}",
                transfer.getId(), transfer.getFromAccountId(), transfer.getToAccountId(), transfer.getAmount(), transfer.getStatus());
    }

    @Async
    @EventListener
    public void handleTransferCompletedNotification(TransferCompletedEvent event) {
        Transfer transfer = event.getTransfer();
        // Simulate sending email/SMS
        logger.info("✉️ [NOTIFICATION SIMULATOR] Sent SMS/Email: 'Your transfer of ${} from account {} to account {} was successful.'",
                transfer.getAmount(), transfer.getFromAccountId(), transfer.getToAccountId());
    }

    @Async
    @EventListener
    public void handleTransferCompletedAnalytics(TransferCompletedEvent event) {
        Transfer transfer = event.getTransfer();
        LocalDate today = LocalDate.now();
        
        synchronized (this) {
            Analytics analytics = analyticsRepository.findByDate(today)
                    .orElseGet(() -> new Analytics(today, 0, BigDecimal.ZERO));

            analytics.setTotalTransfersCount(analytics.getTotalTransfersCount() + 1);
            analytics.setTotalAmountTransferred(analytics.getTotalAmountTransferred().add(transfer.getAmount()));
            analyticsRepository.save(analytics);
        }
        
        logger.info("📊 [ANALYTICS] Updated daily analytics for date: {}.", today);
    }
}
