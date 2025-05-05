package com.company.interfaces.rest;

import com.company.application.FraudTransactionAssessmentResolver;
import com.company.application.FraudTransactionAssessmentService;
import com.company.interfaces.rest.dto.TransactionDto;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class FraudControllerTest {

    @InjectMock
    private FraudTransactionAssessmentResolver fraudTransactionAssessmentResolver;

    @Test
    public void transactionAssessmentShouldReturn401forUnauthorizedRequest() {

    }

    @Test
    public void transactionAssessmentShouldReturnOK() {
        FraudTransactionAssessmentService fraudTransactionAssessmentService =
                Mockito.mock(FraudTransactionAssessmentService.class);
        Mockito.when(fraudTransactionAssessmentResolver.getService("TEST"))
                .thenReturn(fraudTransactionAssessmentService);
        Mockito.when(fraudTransactionAssessmentService.processAssessment(Mockito.mock(TransactionDto.class)))
                        .thenReturn("TOO_HIGH_AMOUNT_TRANSACTION");

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(new TransactionDto("123456",
                        BigDecimal.valueOf(123.23),
                        "PLN",
                        "POL",
                        "TEST"))
                .post("/v1/fraud/transaction-assessment")
                .then()
                .statusCode(200);
    }

    @Test
    public void transactionAssessmentShouldReturn400IfTransactionTypeDoesNotExist() {
        Mockito.when(fraudTransactionAssessmentResolver.getService("TEST"))
                .thenThrow(new IllegalArgumentException("No Service defined for type"));

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(new TransactionDto("123456",
                        BigDecimal.valueOf(123.23),
                        "PLN",
                        "POL",
                        "TEST"))
                .post("/v1/fraud/transaction-assessment")
                .then()
                .statusCode(400);
    }


    @Test
    public void transactionAssessmentShouldReturn500WhenExceptionWasThrownInServiceLayer() {
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(new TransactionDto("123456",
                        BigDecimal.valueOf(123.23),
                        "PLN",
                        "POL",
                        "TEST"))
                .post("/v1/fraud/transaction-assessment")
                .then()
                .statusCode(500);
    }

}
