package com.company.application;

import com.company.application.data.BinData;
import com.company.application.exception.MastercardBinLookupApiException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

@QuarkusTest
public class MasterCardBinLookupServiceWithFallbackTest {

    @Inject
    private MasterCardBinLookupServiceWithFallback underTests;

    @InjectMock
    private MasterCardBinLookupService masterCardBinLookupService;

    @InjectMock
    private RepositoryBinLookupService fallback;


    @Test
    public void getBinDataApiLayerReturnNoResults() {
        String binNum = "123456";
        Mockito.when(masterCardBinLookupService.getBinData(binNum)).thenReturn(Collections.emptyList());
        Mockito.when(fallback.getBinData(binNum)).thenReturn(List.of(new BinData()));

        List<BinData> results = underTests.getBinData(binNum);

        Mockito.verify(fallback, Mockito.times(1)).getBinData(Mockito.anyString());
        Assertions.assertNotNull(results);
        Assertions.assertEquals(1, results.size());
    }

    @Test
    public void getBinDataApiLayerException() {
        String binNum = "123456";
        Mockito.when(masterCardBinLookupService.getBinData(binNum)).thenThrow(new MastercardBinLookupApiException("TEST", null, 400, null, null));
        Mockito.when(fallback.getBinData(binNum)).thenReturn(List.of(new BinData()));

        List<BinData> results = underTests.getBinData(binNum);

        Mockito.verify(fallback, Mockito.times(1)).getBinData(Mockito.anyString());
        Assertions.assertNotNull(results);
        Assertions.assertEquals(1, results.size());
    }

    @Test
    public void getBinDataApiLayerReturnResults() {
        String binNum = "123456";
        Mockito.when(masterCardBinLookupService.getBinData(binNum)).thenReturn(List.of(new BinData(), new BinData()));

        List<BinData> results = underTests.getBinData(binNum);

        Mockito.verifyNoInteractions(fallback);
        Assertions.assertNotNull(results);
        Assertions.assertEquals(2, results.size());
    }
}
