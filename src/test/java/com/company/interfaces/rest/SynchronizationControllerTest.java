package com.company.interfaces.rest;

import com.company.application.BinResourcesSynchronizationService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Named;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class SynchronizationControllerTest {

    @InjectMock
    @Named("mastercardBinResourcesSynchronizationService")
    private BinResourcesSynchronizationService binResourcesSynchronizationService;

    @Test
    @TestSecurity(user = "api-user")
    public void shouldReturn200OkWhenSynchronizationTriggeredSuccessfully() {
        String responseBody = given()
                .when()
                .post("/api/v1/synchronization/binResource")
                .then()
                .statusCode(200)
                .extract()
                .asString();

        Assertions.assertEquals("Synchronization triggered successfully", responseBody);
        Mockito.verify(binResourcesSynchronizationService, Mockito.times(1)).synchronize();
    }

}