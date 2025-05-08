package com.company.application;

import com.company.application.exception.MastercardBinLookupApiException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import lombok.RequiredArgsConstructor;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.BinLookupApi;
import org.openapitools.client.model.BinResource;
import org.openapitools.client.model.BinResourcePage;
import org.openapitools.client.model.SearchByAccountRange;
import org.openapitools.client.model.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class DefaultMastercardBinLookupApi implements MastercardBinLookupApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMastercardBinLookupApi.class);

    private final Instance<BinLookupApi> binLookupApiProvider;

    @Override
    public List<BinResource> searchBy(String binNumber) {
        SearchByAccountRange searchByAccountRange = new SearchByAccountRange();
        searchByAccountRange.setAccountRange(new BigDecimal(binNumber));
        try {
            return binLookupApiProvider.get().searchByAccountRangeResources(searchByAccountRange);
        } catch (ApiException e) {
            LOGGER.error("Exception when calling BinLookupApi#searchByAccountRangeResources");
            throw new MastercardBinLookupApiException(e.getMessage(), e, e.getCode(), e.getResponseHeaders(), e.getResponseBody());
        }
    }

    @Override
    public BinResourcePage getBinResources(List<SearchCriteria> searchCriteria, int page, int size, String sort) {
        try {
            return binLookupApiProvider.get().getBinResources(searchCriteria, page, size, sort);
        } catch (ApiException e) {
            LOGGER.error("Exception when calling BinLookupApi#getBinResources");
            throw new MastercardBinLookupApiException(e.getMessage(), e, e.getCode(), e.getResponseHeaders(), e.getResponseBody());
        }
    }

}
