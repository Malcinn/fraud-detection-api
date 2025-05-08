package com.company.application;

import com.company.application.dto.TransactionAssessment;
import com.company.interfaces.rest.dto.TransactionDto;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

@QuarkusTest
public class WithdrawalFraudTransactionAssessmentServiceTest {

    @Inject
    private WithdrawalFraudTransactionAssessmentService underTests;

    @Test
    public void processAssessmentTransactionBelow1000() {
        TransactionDto transaction = new TransactionDto("123456", BigDecimal.valueOf(456.76), "PLN", "POL", "WITHDRAWAL");
        TransactionAssessment transactionAssessment = underTests.processAssessment(transaction);
        Assertions.assertEquals(0,transactionAssessment.getScore());
        Assertions.assertEquals("LOW_RISK_TRANSACTION_WITHDRAWAL_BELOW_1000",transactionAssessment.getAssessment());
    }

    @Test
    public void processAssessmentTransactionAbove1000() {
        TransactionDto transaction = new TransactionDto("123456", BigDecimal.valueOf(2456.90), "PLN", "POL", "WITHDRAWAL");
        TransactionAssessment transactionAssessment = underTests.processAssessment(transaction);
        Assertions.assertEquals(100,transactionAssessment.getScore());
        Assertions.assertEquals("HIGH_RISK_TRANSACTION_WITHDRAWAL_ABOVE_1000",transactionAssessment.getAssessment());
    }
}
