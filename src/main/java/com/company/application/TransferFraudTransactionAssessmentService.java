package com.company.application;

import com.company.application.data.BinData;
import com.company.application.dto.TransactionAssessment;
import com.company.interfaces.rest.dto.TransactionDto;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class TransferFraudTransactionAssessmentService implements FraudTransactionAssessmentService {

    private static final String LOW_RISK_TRANSACTION = "LOW_RISK_TRANSACTION_AMOUNT_BELOW_1000";
    private static final String MIDDLE_RISK_TRANSACTION_AMOUNT_BELOW_10000 = "MIDDLE_RISK_TRANSACTION_AMOUNT_BELOW_10000";
    private static final String HIGH_RISK_TRANSACTION_AMOUNT_BELOW_50000 = "HIGH_RISK_TRANSACTION_AMOUNT_BELOW_50000";
    private static final String HIGH_RISK_TRANSACTION_AMOUNT_ABOVE_50000 = "HIGH_RISK_TRANSACTION_AMOUNT_ABOVE_50000";
    private static final String HIGH_RISK_TRANSACTION_NO_BIN_DATA_FAKE_OR_FRAUD = "HIGH_RISK_TRANSACTION_NO_BIN_DATA_FAKE_OR_FRAUD";

    private final BinLookupService binLookupService;

    @Override
    public TransactionAssessment processAssessment(TransactionDto transaction) {
        List<BinData> binDataList = binLookupService.getBinData(transaction.getBinNumber());
        if (CollectionUtils.isNotEmpty(binDataList)) {
            if (binDataList.stream()
                    .anyMatch(binData -> binData.getCountryAlpha3().equals(transaction.getCountryAlpha3()))) {
                if (transaction.getAmount().compareTo(BigDecimal.valueOf(1000.0)) <= 0) {
                    return TransactionAssessment.builder().score(0).assessment(LOW_RISK_TRANSACTION).build();
                } else if (transaction.getAmount().compareTo(BigDecimal.valueOf(1000.0)) >= 0 &&
                        transaction.getAmount().compareTo(BigDecimal.valueOf(10000.0)) <= 0) {
                    return TransactionAssessment.builder().score(20).assessment(MIDDLE_RISK_TRANSACTION_AMOUNT_BELOW_10000).build();
                } else if (transaction.getAmount().compareTo(BigDecimal.valueOf(10000.0)) >= 0 &&
                        transaction.getAmount().compareTo(BigDecimal.valueOf(50000.0)) <= 0) {
                    return TransactionAssessment.builder().score(80).assessment(HIGH_RISK_TRANSACTION_AMOUNT_BELOW_50000).build();
                } else {
                    return TransactionAssessment.builder().score(100).assessment(HIGH_RISK_TRANSACTION_AMOUNT_ABOVE_50000).build();
                }
            }
        }
        return TransactionAssessment.builder().score(100).assessment(HIGH_RISK_TRANSACTION_NO_BIN_DATA_FAKE_OR_FRAUD).build();
    }

    @Override
    public String getType() {
        return "TRANSFER";
    }
}
