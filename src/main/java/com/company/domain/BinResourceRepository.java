package com.company.domain;

import java.util.List;

public interface BinResourceRepository {

    List<BinResource> findByBinNum(String binNumber);

    void save(BinResource entity);
}
