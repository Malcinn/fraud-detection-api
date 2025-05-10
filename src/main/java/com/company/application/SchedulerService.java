package com.company.application;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@RequiredArgsConstructor
public class SchedulerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerService.class);

    private final BinResourcesSynchronizationService synchronizationService;

    @Scheduled(cron = "0 0 3 ? * MON")
    public void scheduledSync() {
        LOGGER.info("Scheduled synchronization started...");
        synchronizationService.synchronize();
        LOGGER.info("Scheduled synchronization completed.");
    }
}
