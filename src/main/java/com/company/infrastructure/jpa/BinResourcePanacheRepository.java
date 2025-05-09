package com.company.infrastructure.jpa;

import com.company.domain.BinResource;
import com.company.domain.BinResourceRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class BinResourcePanacheRepository implements BinResourceRepository, PanacheRepository<BinResource> {

    @Override
    public List<BinResource> findByBinNum(String binNumber) {
        return this.find("#BinResource.findAllByBinNum", Parameters.with("binNum", binNumber)).list();
    }

    @Override
    public void save(BinResource entity) {
        this.persist(new BinResource());
    }
}
