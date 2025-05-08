package com.company.application;

import org.openapitools.client.model.BinResource;
import org.openapitools.client.model.BinResourcePage;
import org.openapitools.client.model.SearchCriteria;

import java.util.List;

public interface MastercardBinLookupApi {

    List<BinResource> searchBy(String binNumber);

    BinResourcePage getBinResources(List<SearchCriteria> searchCriteria, int page, int size, String sort);
}
