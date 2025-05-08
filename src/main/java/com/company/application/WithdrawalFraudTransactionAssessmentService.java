package com.company.application;

import com.company.application.dto.TransactionAssessment;
import com.company.interfaces.rest.dto.TransactionDto;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;

@ApplicationScoped
public class WithdrawalFraudTransactionAssessmentService implements FraudTransactionAssessmentService {

    private static final String LOW_RISK_TRANSACTION_BELOW_1000 = "LOW_RISK_TRANSACTION_WITHDRAWAL_BELOW_1000";

    private static final String HIGH_RISK_TRANSACTION_ABOVE_1000 = "HIGH_RISK_TRANSACTION_WITHDRAWAL_ABOVE_1000";

    @Override
    public TransactionAssessment processAssessment(TransactionDto transaction) {
        if (transaction.getAmount().compareTo(BigDecimal.valueOf(1000)) <= 0) {
            return TransactionAssessment.builder().score(0).assessment(LOW_RISK_TRANSACTION_BELOW_1000).build();
        }
        return TransactionAssessment.builder().score(100).assessment(HIGH_RISK_TRANSACTION_ABOVE_1000).build();
    }

    @Override
    public String getType() {
        return "WITHDRAWAL";
    }
}
