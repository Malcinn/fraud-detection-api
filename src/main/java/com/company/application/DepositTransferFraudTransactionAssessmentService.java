package com.company.application;

import com.company.application.data.BinData;
import com.company.application.dto.TransactionAssessment;
import com.company.interfaces.rest.dto.TransactionDto;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class DepositTransferFraudTransactionAssessmentService implements FraudTransactionAssessmentService {

    private static final String LOW_RISK_TRANSACTION = "LOW_RISK_TRANSACTION";

    private static final String HIGH_RISK_INTERNATIONAL_TRANSACTION = "HIGH_RISK_TRANSACTION_INTERNATIONAL_DEPOSIT";

    private static final String HIGH_RISK_TRANSACTION_NO_BIN_DATA = "HIGH_RISK_TRANSACTION_NO_BIN_DATA_FAKE_OR_FRAUD";

    private final BinLookupService binLookupService;

    @Override
    public TransactionAssessment processAssessment(TransactionDto transaction) {
        List<BinData> binDataList = binLookupService.getBinData(transaction.getBinNumber());
        if (CollectionUtils.isNotEmpty(binDataList)) {
            if (binDataList.stream()
                    .anyMatch(binData -> binData.getCountryAlpha3().equals(transaction.getCountryAlpha3()))) {
                return TransactionAssessment.builder().score(0).assessment(LOW_RISK_TRANSACTION).build();
            }
            return TransactionAssessment.builder().score(100).assessment(HIGH_RISK_INTERNATIONAL_TRANSACTION).build();
        }
        return TransactionAssessment.builder().score(100).assessment(HIGH_RISK_TRANSACTION_NO_BIN_DATA).build();
    }

    @Override
    public String getType() {
        return "DEPOSIT";
    }
}
