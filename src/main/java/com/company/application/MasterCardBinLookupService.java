package com.company.application;

import com.company.application.data.BinData;
import com.company.application.data.Mapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.openapitools.client.model.BinResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ApplicationScoped
@Named("masterCardBinLookupService")
@RequiredArgsConstructor
public class MasterCardBinLookupService implements BinLookupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MasterCardBinLookupService.class);

    private final MastercardBinLookupApi binLookupApi;

    private final Mapper<BinResource, BinData> dataMapper;

    @Override
    public List<BinData> getBinData(String binNumber) {
        return binLookupApi.searchBy(binNumber).stream()
                .map(dataMapper::map)
                .toList();
    }

}
