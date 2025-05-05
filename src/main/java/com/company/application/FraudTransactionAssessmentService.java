package com.company.application;

import com.company.interfaces.rest.dto.TransactionDto;

public interface FraudTransactionAssessmentService {

    String processAssessment(TransactionDto transaction);

    String getType();
}
