package com.company.application;

import com.company.application.data.BinData;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ApplicationScoped
@Named("masterCardBinLookupServiceWithFallback")
@RequiredArgsConstructor
public class MasterCardBinLookupServiceWithFallback implements BinLookupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MasterCardBinLookupServiceWithFallback.class);

    private final MasterCardBinLookupService masterCardBinLookupService;

    private final RepositoryBinLookupService fallback;

    @Override
    public List<BinData> getBinData(String binNumber) {
        try {
            List<BinData> results = masterCardBinLookupService.getBinData(binNumber);
            if (CollectionUtils.isEmpty(results)) {
                LOGGER.info("Mastercard Bin Lookup API did not return any results, checking repository");
                return fallback.getBinData(binNumber);
            }
            return results;
        } catch (Exception e) {
            LOGGER.error("Error occurred when calling Mastercard Bin Lookup API, triggering repository fallback");
            return fallback.getBinData(binNumber);
        }
    }
}
