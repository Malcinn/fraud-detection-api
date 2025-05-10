package com.company.application;

import com.company.application.data.Mapper;
import com.company.domain.BinResource;
import com.company.domain.BinResourceRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openapitools.client.model.BinResourcePage;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@QuarkusTest
public class MastercardBinResourcesSynchronizationServiceTest {

    @Inject
    MastercardBinResourcesSynchronizationService underTests;

    @InjectMock
    private MastercardBinLookupApi mastercardBinLookupApi;

    @InjectMock
    private BinResourceRepository binResourceRepository;

    @InjectMock
    private Mapper<org.openapitools.client.model.BinResource, BinResource> mapper;

    @InjectMock
    private UserTransaction userTransaction;

    @Test
    public void shouldHandleEmptyListOfBinResourceItems() {

        BinResourcePage emptyResourcePage = new BinResourcePage();
        emptyResourcePage.setItems(Collections.emptyList());
        emptyResourcePage.setTotalPages(1);
        Mockito.when(mastercardBinLookupApi.getBinResources(Mockito.anyList(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString()))
                .thenReturn(emptyResourcePage);

        underTests.synchronize();

        Mockito.verify(binResourceRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(mapper, Mockito.never()).map(Mockito.any());
    }

    @Test
    public void shouldHandleSinglePageOfBinResourceItems() throws Exception {
        BinResource binResource1 = new BinResource();
        binResource1.setBinNum(123456);
        binResource1.setLowAccountRange(1000);
        binResource1.setHighAccountRange(2000);

        BinResource binResource2 = new BinResource();
        binResource2.setBinNum(654321);
        binResource2.setLowAccountRange(3000);
        binResource2.setHighAccountRange(4000);

        List<org.openapitools.client.model.BinResource> apiResources = List.of(
                new org.openapitools.client.model.BinResource().binNum("123456").lowAccountRange(1000).highAccountRange(2000),
                new org.openapitools.client.model.BinResource().binNum("654321").lowAccountRange(3000).highAccountRange(4000)
        );

        BinResourcePage resourcePage = new BinResourcePage();
        resourcePage.setItems(apiResources);
        resourcePage.setTotalPages(1);

        Mockito.when(mastercardBinLookupApi.getBinResources(Mockito.anyList(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString()))
                .thenReturn(resourcePage);

        Mockito.when(mapper.map(apiResources.get(0))).thenReturn(binResource1);
        Mockito.when(mapper.map(apiResources.get(1))).thenReturn(binResource2);

        Mockito.when(binResourceRepository.findByUniqueParams(123456, 1000, 2000)).thenReturn(Optional.empty());
        Mockito.when(binResourceRepository.findByUniqueParams(654321, 3000, 4000)).thenReturn(Optional.empty());

        underTests.synchronize();

        Mockito.verify(binResourceRepository, Mockito.times(1)).save(binResource1);
        Mockito.verify(binResourceRepository, Mockito.times(1)).save(binResource2);
        Mockito.verify(userTransaction, Mockito.times(2)).begin();
        Mockito.verify(userTransaction, Mockito.times(2)).commit();
    }

    @Test
    public void shouldHandleMultiplePagesOfBinResourceItemsCorrectly() throws Exception {
        BinResource binResource1 = new BinResource();
        binResource1.setBinNum(123456);
        binResource1.setLowAccountRange(1000);
        binResource1.setHighAccountRange(2000);

        BinResource binResource2 = new BinResource();
        binResource2.setBinNum(654321);
        binResource2.setLowAccountRange(3000);
        binResource2.setHighAccountRange(4000);

        List<org.openapitools.client.model.BinResource> apiResourcesPage1 = List.of(
                new org.openapitools.client.model.BinResource().binNum("123456").lowAccountRange(1000).highAccountRange(2000)
        );

        List<org.openapitools.client.model.BinResource> apiResourcesPage2 = List.of(
                new org.openapitools.client.model.BinResource().binNum("654321").lowAccountRange(3000).highAccountRange(4000)
        );

        BinResourcePage resourcePage1 = new BinResourcePage();
        resourcePage1.setItems(apiResourcesPage1);
        resourcePage1.setTotalPages(2);

        BinResourcePage resourcePage2 = new BinResourcePage();
        resourcePage2.setItems(apiResourcesPage2);
        resourcePage2.setTotalPages(2);

        Mockito.when(mastercardBinLookupApi.getBinResources(Mockito.anyList(), Mockito.eq(1), Mockito.anyInt(), Mockito.anyString()))
                .thenReturn(resourcePage1);
        Mockito.when(mastercardBinLookupApi.getBinResources(Mockito.anyList(), Mockito.eq(2), Mockito.anyInt(), Mockito.anyString()))
                .thenReturn(resourcePage2);

        Mockito.when(mapper.map(apiResourcesPage1.get(0))).thenReturn(binResource1);
        Mockito.when(mapper.map(apiResourcesPage2.get(0))).thenReturn(binResource2);

        Mockito.when(binResourceRepository.findByUniqueParams(123456, 1000, 2000)).thenReturn(Optional.empty());
        Mockito.when(binResourceRepository.findByUniqueParams(654321, 3000, 4000)).thenReturn(Optional.empty());

        underTests.synchronize();

        Mockito.verify(binResourceRepository, Mockito.times(1)).save(binResource1);
        Mockito.verify(binResourceRepository, Mockito.times(1)).save(binResource2);
        Mockito.verify(userTransaction, Mockito.times(2)).begin();
        Mockito.verify(userTransaction, Mockito.times(2)).commit();
    }

    @Test
    public void shouldHandleNullTotalPagesInBinResourcePage() throws Exception {
        BinResource binResource1 = new BinResource();
        binResource1.setBinNum(123456);
        binResource1.setLowAccountRange(1000);
        binResource1.setHighAccountRange(2000);

        List<org.openapitools.client.model.BinResource> apiResources = List.of(
                new org.openapitools.client.model.BinResource().binNum("123456").lowAccountRange(1000).highAccountRange(2000)
        );

        BinResourcePage resourcePage = new BinResourcePage();
        resourcePage.setItems(apiResources);
        resourcePage.setTotalPages(null); // Simulate null totalPages

        Mockito.when(mastercardBinLookupApi.getBinResources(Mockito.anyList(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString()))
                .thenReturn(resourcePage);

        Mockito.when(mapper.map(apiResources.get(0))).thenReturn(binResource1);

        Mockito.when(binResourceRepository.findByUniqueParams(123456, 1000, 2000)).thenReturn(Optional.empty());

        underTests.synchronize();

        Mockito.verify(binResourceRepository, Mockito.times(1)).save(binResource1);
        Mockito.verify(userTransaction, Mockito.times(1)).begin();
        Mockito.verify(userTransaction, Mockito.times(1)).commit();
    }

    @Test
    public void shouldRollbackTransactionAndLogErrorWhenBeginThrowsException() throws Exception {
        List<org.openapitools.client.model.BinResource> apiResources = List.of(
                new org.openapitools.client.model.BinResource().binNum("123456").lowAccountRange(1000).highAccountRange(2000)
        );

        BinResourcePage resourcePage = new BinResourcePage();
        resourcePage.setItems(apiResources);
        resourcePage.setTotalPages(1);

        Mockito.when(mastercardBinLookupApi.getBinResources(Mockito.anyList(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString()))
                .thenReturn(resourcePage);

        Mockito.doThrow(new SystemException("Begin transaction failed")).when(userTransaction).begin();

        underTests.synchronize();

        Mockito.verify(userTransaction, Mockito.times(1)).begin();
        Mockito.verify(userTransaction, Mockito.times(1)).rollback();
        Mockito.verify(binResourceRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(mapper, Mockito.never()).map(Mockito.any());
    }

    @Test
    public void shouldRollbackTransactionAndLogErrorWhenCreateOrUpdateThrowsException() throws Exception {
        List<org.openapitools.client.model.BinResource> apiResources = List.of(
                new org.openapitools.client.model.BinResource().binNum("123456").lowAccountRange(1000).highAccountRange(2000)
        );

        BinResourcePage resourcePage = new BinResourcePage();
        resourcePage.setItems(apiResources);
        resourcePage.setTotalPages(1);

        Mockito.when(mastercardBinLookupApi.getBinResources(Mockito.anyList(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString()))
                .thenReturn(resourcePage);

        Mockito.doThrow(new RuntimeException("Create or update failed")).when(mapper).map(apiResources.get(0));

        underTests.synchronize();

        Mockito.verify(userTransaction, Mockito.times(1)).begin();
        Mockito.verify(userTransaction, Mockito.times(1)).rollback();
        Mockito.verify(binResourceRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(mapper, Mockito.times(1)).map(apiResources.get(0));
    }

    @Test
    public void shouldCorrectlyIncrementPageNumberAndContinueFetchingWhenMorePagesAreAvailable() throws Exception {
        BinResource binResource1 = new BinResource();
        binResource1.setBinNum(123456);
        binResource1.setLowAccountRange(1000);
        binResource1.setHighAccountRange(2000);

        BinResource binResource2 = new BinResource();
        binResource2.setBinNum(654321);
        binResource2.setLowAccountRange(3000);
        binResource2.setHighAccountRange(4000);

        List<org.openapitools.client.model.BinResource> apiResourcesPage1 = List.of(
                new org.openapitools.client.model.BinResource().binNum("123456").lowAccountRange(1000).highAccountRange(2000)
        );

        List<org.openapitools.client.model.BinResource> apiResourcesPage2 = List.of(
                new org.openapitools.client.model.BinResource().binNum("654321").lowAccountRange(3000).highAccountRange(4000)
        );

        BinResourcePage resourcePage1 = new BinResourcePage();
        resourcePage1.setItems(apiResourcesPage1);
        resourcePage1.setTotalPages(2);

        BinResourcePage resourcePage2 = new BinResourcePage();
        resourcePage2.setItems(apiResourcesPage2);
        resourcePage2.setTotalPages(2);

        Mockito.when(mastercardBinLookupApi.getBinResources(Mockito.anyList(), Mockito.eq(1), Mockito.anyInt(), Mockito.anyString()))
                .thenReturn(resourcePage1);
        Mockito.when(mastercardBinLookupApi.getBinResources(Mockito.anyList(), Mockito.eq(2), Mockito.anyInt(), Mockito.anyString()))
                .thenReturn(resourcePage2);

        Mockito.when(mapper.map(apiResourcesPage1.get(0))).thenReturn(binResource1);
        Mockito.when(mapper.map(apiResourcesPage2.get(0))).thenReturn(binResource2);

        Mockito.when(binResourceRepository.findByUniqueParams(123456, 1000, 2000)).thenReturn(Optional.empty());
        Mockito.when(binResourceRepository.findByUniqueParams(654321, 3000, 4000)).thenReturn(Optional.empty());

        underTests.synchronize();

        Mockito.verify(binResourceRepository, Mockito.times(1)).save(binResource1);
        Mockito.verify(binResourceRepository, Mockito.times(1)).save(binResource2);
        Mockito.verify(userTransaction, Mockito.times(2)).begin();
        Mockito.verify(userTransaction, Mockito.times(2)).commit();
        Mockito.verify(mastercardBinLookupApi, Mockito.times(1)).getBinResources(Mockito.anyList(), Mockito.eq(1), Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(mastercardBinLookupApi, Mockito.times(1)).getBinResources(Mockito.anyList(), Mockito.eq(2), Mockito.anyInt(), Mockito.anyString());
    }

    @Test
    public void shouldHandleBinResourcePageWithNullItemsListGracefully() throws Exception {
        BinResourcePage resourcePage = new BinResourcePage();
        resourcePage.setItems(null); // Simulate null items list
        resourcePage.setTotalPages(1);

        Mockito.when(mastercardBinLookupApi.getBinResources(Mockito.anyList(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString()))
                .thenReturn(resourcePage);

        underTests.synchronize();

        Mockito.verify(binResourceRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(mapper, Mockito.never()).map(Mockito.any());
        Mockito.verify(userTransaction, Mockito.never()).begin();
        Mockito.verify(userTransaction, Mockito.never()).commit();
    }
}