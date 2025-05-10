package com.company.application;

import com.company.application.data.BinData;
import com.company.application.data.Mapper;
import com.company.domain.BinResource;
import com.company.domain.BinResourceRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationScoped
@Named("repositoryBinLookupService")
@RequiredArgsConstructor
public class RepositoryBinLookupService implements BinLookupService {

    private final BinResourceRepository binResourceRepository;

    private final Mapper<BinResource, BinData> mapper;

    @Override
    public List<BinData> getBinData(String binNumber) {
        return binResourceRepository.findByBinNum(binNumber).stream()
                .map(mapper::map).toList();
    }
}
