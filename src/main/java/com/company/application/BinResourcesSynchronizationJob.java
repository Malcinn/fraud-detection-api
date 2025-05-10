package com.company.application;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class BinResourcesSynchronizationJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(BinResourcesSynchronizationJob.class);

    private final BinResourcesSynchronizationService binResourcesSynchronizationService;

    public void execute() {
        LOGGER.info("### SYNCHRONIZATION CRON JOB STARTED ###");
        LOGGER.info("Synchronize BinResources entities from external API");
        binResourcesSynchronizationService.synchronize();
        LOGGER.info("### SYNCHRONIZATION FINISHED ###");
    }
}
