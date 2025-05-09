package com.company.application;

import com.company.application.data.BinData;
import com.company.application.exception.MastercardBinLookupApiException;
import com.company.domain.BinResource;
import com.company.domain.BinResourceRepository;
import io.quarkus.panache.common.exception.PanacheQueryException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

@QuarkusTest
public class RepositoryBinLookupServiceTest {

    @Inject
    private RepositoryBinLookupService underTests;

    @InjectMock
    private BinResourceRepository binResourceRepository;

    @InjectMock
    private Mapper<BinResource, BinData> dataMapper;

    @Test
    public void getBinDataRepositoryReturnNoResults() {
        String binNum = "123456";
        Mockito.when(binResourceRepository.findByBinNum(binNum)).thenReturn(Collections.emptyList());
        Mockito.when(dataMapper.map(Mockito.any(BinResource.class))).thenReturn(new BinData());

        List<BinData> results = underTests.getBinData(binNum);

        Assertions.assertNotNull(results);
        Assertions.assertEquals(0, results.size());
    }

    @Test
    public void getBinDataApiLayerException() {
        String binNum = "123456";
        Mockito.when(binResourceRepository.findByBinNum(binNum)).thenThrow(PanacheQueryException.class);
        try {
            List<BinData> results = underTests.getBinData(binNum);
        } catch (Exception e) {
            Assertions.assertEquals(PanacheQueryException.class, e.getClass());
        }
    }

    @Test
    public void getBinDataApiLayerReturnResults() {
        String binNum = "123456";
        List<BinResource> resources = List.of(new BinResource(), new BinResource());
        Mockito.when(binResourceRepository.findByBinNum(binNum)).thenReturn(resources);
        Mockito.when(dataMapper.map(Mockito.any(BinResource.class))).thenReturn(new BinData());

        List<BinData> results = underTests.getBinData(binNum);

        Assertions.assertNotNull(results);
        Assertions.assertEquals(2, results.size());
    }
}
