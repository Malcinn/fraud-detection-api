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

    private final Instance<BinLookupApi> binLookupApiProvider;

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
        SearchByAccountRange searchByAccountRange = new SearchByAccountRange();
        searchByAccountRange.setAccountRange(new BigDecimal(binNumber));
        try {
            List<BinResource> result = binLookupApiProvider.get().searchByAccountRangeResources(searchByAccountRange);
            System.out.println(result);
            return result;
        } catch (ApiException e) {
            LOGGER.error("Exception when calling BinLookupApi#searchByAccountRangeResources");
            LOGGER.error("Status code: " + e.getCode());
            LOGGER.error("Reason: " + e.getResponseBody());
            LOGGER.error("Response headers: " + e.getResponseHeaders());
            throw new ResourceNotFoundException(e.getMessage(), e);
        }
    }

    public BinData getBinData2(String binNumber) {
        List<SearchCriteria> searchCriteria = Arrays.asList(); // List<SearchCriteria> | BIN Resource Search Request
        Integer page = 1; // Integer | The current page based on the number of items per page. Use this value to iterate through all of the paginated data and to know when you are the end of the data.
        Integer size = 25; // Integer | The number of items to be displayed per page. The larger the number the slower the API will respond, but the less requests you will need to make for paginating through data. We recommend trying a page size of 1000 and adjusting for your needs.
        String sort = "-lowAccountRange"; // String | Sort by any parameter that the API returns with a direction (- for descending sort, + for ascending sort). For example -lowAccountRange will present the returned data in descending order based on the low account range values. The same is true for any of parameters such as customerName (+customerName) or ica (-ica).
        try {

            BinResourcePage result = binLookupApiProvider.get().getBinResources(searchCriteria, page, size, sort);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling BinLookupApi#getBinResources");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }

        return null;
    }
}
