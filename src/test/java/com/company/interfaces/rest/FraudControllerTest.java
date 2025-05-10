package com.company.interfaces.rest;

import com.company.application.FraudTransactionAssessmentResolver;
import com.company.application.FraudTransactionAssessmentService;
import com.company.application.dto.TransactionAssessment;
import com.company.interfaces.rest.dto.TransactionDto;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class FraudControllerTest {

    @InjectMock
    private FraudTransactionAssessmentResolver fraudTransactionAssessmentResolver;

    @Test
    public void transactionAssessmentShouldReturn401forUnauthorizedRequest() {
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(new TransactionDto("123456",
                        BigDecimal.valueOf(123.23),
                        "PLN",
                        "POL",
                        "TEST"))
                .post("/api/v1/fraud/transaction-assessment")
                .then()
                .statusCode(401);
    }

    @Test
    @TestSecurity(user = "api-user")
    public void transactionAssessmentShouldReturnOK() throws Exception {
        FraudTransactionAssessmentService fraudTransactionAssessmentService =
                Mockito.mock(FraudTransactionAssessmentService.class);
        Mockito.when(fraudTransactionAssessmentResolver.getService("TEST"))
                .thenReturn(fraudTransactionAssessmentService);
        Mockito.when(fraudTransactionAssessmentService.processAssessment(Mockito.mock(TransactionDto.class)))
                .thenReturn(TransactionAssessment.builder().score(100).assessment("TOO_HIGH_AMOUNT_TRANSACTION").build());

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(new TransactionDto("123456",
                        BigDecimal.valueOf(123.23),
                        "PLN",
                        "POL",
                        "TEST"))
                .post("/api/v1/fraud/transaction-assessment")
                .then()
                .statusCode(200);
    }

    @Test
    @TestSecurity(user = "api-user")
    public void transactionAssessmentShouldReturn400IfTransactionTypeDoesNotExist() throws Exception {
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
                .post("/api/v1/fraud/transaction-assessment")
                .then()
                .statusCode(400);
    }

    @Test
    @TestSecurity(user = "api-user")
    public void transactionAssessmentShouldReturn500WhenExceptionWasThrownInServiceLayer() throws Exception {

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(new TransactionDto("123456",
                        BigDecimal.valueOf(123.23),
                        "PLN",
                        "POL",
                        "TEST"))
                .post("/api/v1/fraud/transaction-assessment")
                .then()
                .statusCode(500);
    }
}
