package com.company.application;

import com.company.application.data.BinData;
import com.company.application.dto.TransactionAssessment;
import com.company.interfaces.rest.dto.TransactionDto;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@QuarkusTest
public class TransferFraudTransactionAssessmentServiceTest {

    @Inject
    private TransferFraudTransactionAssessmentService underTests;

    @InjectMock
    private BinLookupService binLookupService;

    @Test
    public void processAssessmentTransactionBelow1000() {
        BinData binData = BinData.builder().binNum("123456").countryAlpha3("POL").build();
        Mockito.when(binLookupService.getBinData(Mockito.anyString())).thenReturn(List.of(binData));

        TransactionDto transaction = new TransactionDto("123456", BigDecimal.valueOf(123.23), "PLN", "POL", "TRANSFER");
        TransactionAssessment transactionAssessment = underTests.processAssessment(transaction);
        Assertions.assertEquals(0, transactionAssessment.getScore());
        Assertions.assertEquals("LOW_RISK_TRANSACTION_AMOUNT_BELOW_1000", transactionAssessment.getAssessment());
    }

    @Test
    public void processAssessmentTransactionBelow10000() {
        BinData binData = BinData.builder().binNum("123456").countryAlpha3("POL").build();
        Mockito.when(binLookupService.getBinData(Mockito.anyString())).thenReturn(List.of(binData));

        TransactionDto transaction = new TransactionDto("123456", BigDecimal.valueOf(8459.23), "PLN", "POL", "TRANSFER");
        TransactionAssessment transactionAssessment = underTests.processAssessment(transaction);
        Assertions.assertEquals(20, transactionAssessment.getScore());
        Assertions.assertEquals("MIDDLE_RISK_TRANSACTION_AMOUNT_BELOW_10000", transactionAssessment.getAssessment());
    }

    @Test
    public void processAssessmentTransactionAbove10000AndBelow50000() {
        BinData binData = BinData.builder().binNum("123456").countryAlpha3("POL").build();
        Mockito.when(binLookupService.getBinData(Mockito.anyString())).thenReturn(List.of(binData));

        TransactionDto transaction = new TransactionDto("123456", BigDecimal.valueOf(36876.23), "PLN", "POL", "TRANSFER");
        TransactionAssessment transactionAssessment = underTests.processAssessment(transaction);
        Assertions.assertEquals(80, transactionAssessment.getScore());
        Assertions.assertEquals("HIGH_RISK_TRANSACTION_AMOUNT_BELOW_50000", transactionAssessment.getAssessment());
    }

    @Test
    public void processAssessmentTransactionAbove50000() {
        BinData binData = BinData.builder().binNum("123456").countryAlpha3("POL").build();
        Mockito.when(binLookupService.getBinData(Mockito.anyString())).thenReturn(List.of(binData));

        TransactionDto transaction = new TransactionDto("123456", BigDecimal.valueOf(90001.25), "PLN", "POL", "TRANSFER");
        TransactionAssessment transactionAssessment = underTests.processAssessment(transaction);
        Assertions.assertEquals(100, transactionAssessment.getScore());
        Assertions.assertEquals("HIGH_RISK_TRANSACTION_AMOUNT_ABOVE_50000", transactionAssessment.getAssessment());
    }

    @Test
    public void processAssessmentNoBinData() {
        Mockito.when(binLookupService.getBinData(Mockito.anyString())).thenReturn(Collections.emptyList());

        TransactionDto transaction = new TransactionDto("123456", BigDecimal.valueOf(10000), "PLN", "POL", "TRANSFER");
        TransactionAssessment transactionAssessment = underTests.processAssessment(transaction);
        Assertions.assertEquals(100, transactionAssessment.getScore());
        Assertions.assertEquals("HIGH_RISK_TRANSACTION_NO_BIN_DATA_FAKE_OR_FRAUD", transactionAssessment.getAssessment());
    }
}
