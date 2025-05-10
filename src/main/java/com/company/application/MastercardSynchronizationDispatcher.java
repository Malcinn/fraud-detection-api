package com.company.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@ApplicationScoped
@Named("mastercardSynchronizationDispatcher")
public class MastercardSynchronizationDispatcher implements BinResourcesSynchronizationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MastercardSynchronizationDispatcher.class);

    private final BinResourcesSynchronizationService synchronizationService;

    private final Executor executor;

    @Inject
    public MastercardSynchronizationDispatcher(@Named("mastercardBinResourcesSynchronizationService")
                                               BinResourcesSynchronizationService binResourcesSynchronizationService,
                                               Executor executor) {
        this.synchronizationService = binResourcesSynchronizationService;
        this.executor = executor;
    }


    @Override
    public void synchronize() {
        LOGGER.info("Mastercard synchronization async task started...");
        CompletableFuture.runAsync(() -> {
            synchronizationService.synchronize();
        }, executor);
    }
}
