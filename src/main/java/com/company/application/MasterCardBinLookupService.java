package com.company.application;

import com.company.application.data.BinData;
import com.company.application.exception.ResourceNotFoundException;
import com.company.interfaces.rest.exception.ExceptionHandler;
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
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class MasterCardBinLookupService implements BinLookupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MasterCardBinLookupService.class);

    private final MastercardBinLookupApi binLookupApi;

    private final Mapper<BinResource, BinData> dataMapper;

    @Override
    public BinData getBinData(String binNumber) {
        List<BinResource> result = getRawBinData(binNumber);
        if (result.size() > 1){
            throw new ResourceNotFoundException("asdasd");
        }
        return dataMapper.map(result.getFirst());
    }

    public List<BinResource> getRawBinData(String binNumber){
        List<BinResource> result = binLookupApi.searchBy(binNumber);
        return null;
    }

}
