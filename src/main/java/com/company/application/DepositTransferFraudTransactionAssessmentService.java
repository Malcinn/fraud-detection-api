package com.company.application;

import com.company.application.dto.TransactionAssessment;
import com.company.interfaces.rest.dto.TransactionDto;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DepositTransferFraudTransactionAssessmentService implements FraudTransactionAssessmentService {

    @Override
    public TransactionAssessment processAssessment(TransactionDto transaction) {
        return null;
    }

    @Override
    public String getType() {
        return "DEPOSIT";
    }
}
