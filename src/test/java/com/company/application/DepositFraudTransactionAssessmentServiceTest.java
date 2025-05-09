package com.company.application;

import com.company.application.data.BinData;
import com.company.application.dto.TransactionAssessment;
import com.company.interfaces.rest.dto.TransactionDto;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@QuarkusTest
public class DepositFraudTransactionAssessmentServiceTest {

    private DepositTransferFraudTransactionAssessmentService underTests;

    private BinLookupService binLookupService;

    @BeforeEach
    public void setup() {
        binLookupService = Mockito.mock(BinLookupService.class);
        underTests = new DepositTransferFraudTransactionAssessmentService(binLookupService);
    }

    @Test
    public void processAssessmentNoBinData() {
        Mockito.when(binLookupService.getBinData(Mockito.anyString())).thenReturn(Collections.emptyList());

        TransactionDto transaction = new TransactionDto("123456", BigDecimal.valueOf(1000), "PLN", "POL", "DEPOSIT");
        TransactionAssessment transactionAssessment = underTests.processAssessment(transaction);
        Assertions.assertEquals(100, transactionAssessment.getScore());
        Assertions.assertEquals("HIGH_RISK_TRANSACTION_NO_BIN_DATA_FAKE_OR_FRAUD", transactionAssessment.getAssessment());
    }

    @Test
    public void processAssessmentNationalTransaction() {
        BinData binData = BinData.builder().binNum("123456").countryAlpha3("POL").build();
        Mockito.when(binLookupService.getBinData(Mockito.anyString())).thenReturn(List.of(binData));

        TransactionDto transaction = new TransactionDto("123456", BigDecimal.valueOf(10000), "PLN", "POL", "DEPOSIT");
        TransactionAssessment transactionAssessment = underTests.processAssessment(transaction);
        Assertions.assertEquals(0, transactionAssessment.getScore());
        Assertions.assertEquals("LOW_RISK_TRANSACTION", transactionAssessment.getAssessment());
    }

    @Test
    public void processAssessmentInternationalTransaction() {
        BinData binData = BinData.builder().binNum("123456").countryAlpha3("USA").build();
        Mockito.when(binLookupService.getBinData(Mockito.anyString())).thenReturn(List.of(binData));

        TransactionDto transaction = new TransactionDto("123456", BigDecimal.valueOf(100000), "PLN", "POL", "DEPOSIT");
        TransactionAssessment transactionAssessment = underTests.processAssessment(transaction);
        Assertions.assertEquals(100, transactionAssessment.getScore());
        Assertions.assertEquals("HIGH_RISK_TRANSACTION_INTERNATIONAL_DEPOSIT", transactionAssessment.getAssessment());
    }
}
