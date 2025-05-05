package com.company.configuration;

import com.company.application.DepositTransferFraudTransactionAssessmentService;
import com.company.application.FraudTransactionAssessmentService;
import com.company.application.TransferFraudTransactionAssessmentService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class ApplicationConfiguration {

    private final TransferFraudTransactionAssessmentService transferFraudTransactionAssessmentService;

    private final DepositTransferFraudTransactionAssessmentService depositTransferFraudTransactionAssessmentService;

    @Produces
    @ApplicationScoped
    public List<FraudTransactionAssessmentService> fraudTransactionAssessmentServices() {
        return List.of(transferFraudTransactionAssessmentService, depositTransferFraudTransactionAssessmentService);
    }
}
