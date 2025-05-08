package com.company.application;

import com.company.application.data.BinData;

import java.util.List;

public interface BinLookupService {

    List<BinData> getBinData(String binNumber);
}
