package com.company.application;

import com.company.interfaces.rest.dto.TransactionDto;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DepositTransferFraudTransactionAssessmentService implements FraudTransactionAssessmentService {

    @Override
    public String processAssessment(TransactionDto transaction) {
        return "";
    }

    @Override
    public String getType() {
        return "DEPOSIT";
    }
}
