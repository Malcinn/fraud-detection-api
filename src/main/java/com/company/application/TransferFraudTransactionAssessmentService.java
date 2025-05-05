package com.company.application;

import com.company.interfaces.rest.dto.TransactionDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Named;

@ApplicationScoped
public class TransferFraudTransactionAssessmentService implements FraudTransactionAssessmentService {
    @Override
    public String processAssessment(TransactionDto transaction) {
        return "";
    }

    @Override
    public String getType() {
        return "TRANSFER";
    }
}
