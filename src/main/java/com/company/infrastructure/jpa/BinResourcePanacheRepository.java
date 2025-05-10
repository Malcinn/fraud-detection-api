package com.company.infrastructure.jpa;

import com.company.domain.BinResource;
import com.company.domain.BinResourceRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class BinResourcePanacheRepository implements BinResourceRepository, PanacheRepository<BinResource> {

    @Override
    public List<BinResource> findByBinNum(String binNumber) {
        return this.find("#BinResource.findAllByBinNum", Parameters.with("binNum", binNumber)).list();
    }

    public Optional<BinResource> findByUniqueParams(Integer binNumber, Integer lowAccountRange, Integer highAccountRange) {
        return this.find("#BinResource.findByUniqueParams", Parameters.with("binNum", binNumber)
                .and("lowAccountRange", lowAccountRange)
                .and("highAccountRange", highAccountRange)).firstResultOptional();
    }

    @Override
    public void save(BinResource entity) {
        this.persist(entity);
    }

}
