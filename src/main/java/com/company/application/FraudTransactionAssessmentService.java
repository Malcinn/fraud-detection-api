package com.company.application;

import com.company.application.dto.TransactionAssessment;
import com.company.interfaces.rest.dto.TransactionDto;

public interface FraudTransactionAssessmentService {

    TransactionAssessment processAssessment(TransactionDto transaction);

    String getType();
}
