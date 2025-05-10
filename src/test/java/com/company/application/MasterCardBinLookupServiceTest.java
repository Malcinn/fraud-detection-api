package com.company.application;

import com.company.application.data.BinData;
import com.company.application.data.Mapper;
import com.company.application.exception.MastercardBinLookupApiException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openapitools.client.model.BinResource;

import java.util.Collections;
import java.util.List;

@QuarkusTest
public class MasterCardBinLookupServiceTest {

    @Inject
    private MasterCardBinLookupService underTests;

    @InjectMock
    private MastercardBinLookupApi binLookupApi;

    @InjectMock
    private Mapper<BinResource, BinData> dataMapper;

    @Test
    public void getBinDataApiLayerReturnNoResults() {
        String binNum = "123456";
        Mockito.when(binLookupApi.searchBy(binNum)).thenReturn(Collections.emptyList());
        Mockito.when(dataMapper.map(Mockito.any(BinResource.class))).thenReturn(new BinData());

        List<BinData> results = underTests.getBinData(binNum);

        Assertions.assertNotNull(results);
        Assertions.assertEquals(0, results.size());
    }

    @Test
    public void getBinDataApiLayerException() {
        String binNum = "123456";
        Mockito.when(binLookupApi.searchBy(binNum)).thenThrow(new MastercardBinLookupApiException("TEST", null, 400, null, null));
        try {
            List<BinData> results = underTests.getBinData(binNum);
        } catch (Exception e) {
            Assertions.assertEquals(MastercardBinLookupApiException.class, e.getClass());
        }
    }

    @Test
    public void getBinDataApiLayerReturnResults() {
        String binNum = "123456";
        List<BinResource> resources = List.of(new BinResource(), new BinResource());
        Mockito.when(binLookupApi.searchBy(binNum)).thenReturn(resources);
        Mockito.when(dataMapper.map(Mockito.any(BinResource.class))).thenReturn(new BinData());

        List<BinData> results = underTests.getBinData(binNum);

        Assertions.assertNotNull(results);
        Assertions.assertEquals(2, results.size());
    }
}
