package com.company.application;

import com.company.application.data.BinData;
import com.company.application.dto.TransactionAssessment;
import com.company.interfaces.rest.dto.TransactionDto;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@ApplicationScoped
@RequiredArgsConstructor
public class TransferFraudTransactionAssessmentService implements FraudTransactionAssessmentService {

    private final BinLookupService binLookupService;

    @Override
    public TransactionAssessment processAssessment(TransactionDto transaction) {
        BinData binData = binLookupService.getBinData(transaction.getBinNumber());
        if (Objects.nonNull(binData)) {
            return TransactionAssessment.builder().score(0).assessment("SAFE_TRANSFER_TRANSACTION").build();
        }
        return TransactionAssessment.builder().score(100).assessment("NO_BIN_INFO_HIGH_RISK_TRANSACTION").build();
    }

    @Override
    public String getType() {
        return "TRANSFER";
    }
}
