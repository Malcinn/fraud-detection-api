package com.company.application;

import com.company.application.data.Mapper;
import com.company.domain.BinResource;
import com.company.domain.BinResourceRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.openapitools.client.model.BinResourcePage;
import org.openapitools.client.model.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
@Named("mastercardBinResourcesSynchronizationService")
public class MastercardBinResourcesSynchronizationService implements BinResourcesSynchronizationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MastercardBinResourcesSynchronizationService.class);

    private final MastercardBinLookupApi mastercardBinLookupApi;

    private final BinResourceRepository binResourceRepository;

    private final Mapper<org.openapitools.client.model.BinResource, BinResource> mapper;

    private final UserTransaction userTransaction;

    @Override
    public void synchronize() {
        List<SearchCriteria> searchCriteria = Collections.emptyList();
        int page = 1;
        int totalPages = 1;
        int size = 5000;
        String sort = "-lowAccountRange";
        do {
            BinResourcePage resourcePage = mastercardBinLookupApi.getBinResources(searchCriteria, page, size, sort);
            totalPages = Objects.nonNull(resourcePage.getTotalPages()) ? resourcePage.getTotalPages() : 1;
            LOGGER.info("Processing page {} of {} with {} bin resources", page, totalPages, resourcePage.getItems().size());

            if (CollectionUtils.isNotEmpty(resourcePage.getItems())) {
                resourcePage.getItems().forEach(binResource -> {
                    try {
                        userTransaction.begin();
                        createOrUpdate(binResource);
                        userTransaction.commit();
                    } catch (Exception e) {
                        LOGGER.error("Error occurred while processing bin resource: {}, message: {}", binResource.getBinNum(), e.getMessage());
                        LOGGER.error(binResource.toJson());
                        try {
                            userTransaction.rollback();
                        } catch (SystemException rollbackEx) {
                            LOGGER.error("Rollback failed for bin resource: {}, message: {}", binResource.getBinNum(), rollbackEx.getMessage(), rollbackEx);
                        }
                    }
                });
            }
        } while (++page <= totalPages);
    }

    private void createOrUpdate(org.openapitools.client.model.BinResource binResource) {
        assert binResource.getBinNum() != null;
        Optional<BinResource> model = binResourceRepository.findByUniqueParams(Integer.valueOf(binResource.getBinNum()),
                binResource.getLowAccountRange(), binResource.getHighAccountRange());
        if (model.isPresent()) {
            BinResource existing = model.get();
            existing.updateFrom(mapper.map(binResource));
            binResourceRepository.save(existing);
        } else {
            binResourceRepository.save(mapper.map(binResource));
        }
    }
}
