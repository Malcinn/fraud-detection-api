package com.company.domain;

import java.util.List;
import java.util.Optional;

public interface BinResourceRepository {

    List<BinResource> findByBinNum(String binNumber);

    Optional<BinResource> findByUniqueParams(Integer binNumber, Integer lowAccountRange, Integer highAccountRange);

    void save(BinResource entity);

}
