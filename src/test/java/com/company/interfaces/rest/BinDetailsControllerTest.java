package com.company.interfaces.rest;

import com.company.application.MastercardBinLookupApi;
import com.company.interfaces.rest.dto.AccountRange;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openapitools.client.model.BinResource;

import java.util.List;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class BinDetailsControllerTest {

    @InjectMock
    private MastercardBinLookupApi mastercardBinLookupApi;

    @Test
    public void binDetailsShouldShouldReturn400IfBinNumberHasWrongFormat() {
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(new AccountRange("000123456789"))
                .post("/v1/bin-details/mastercard")
                .then()
                .statusCode(400);
    }

    @Test
    public void binDetailsShould500WhenExceptionWasThrownInServiceLayer() {
        Mockito.when(mastercardBinLookupApi.searchBy(Mockito.anyString())).thenThrow(NullPointerException.class);

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(new AccountRange("123456"))
                .post("/v1/bin-details/mastercard")
                .then()
                .statusCode(500);
    }

    @Test
    public void binDetailsShould200OK() {
        BinResource binResource = new BinResource();
        binResource.setBinNum("123456");
        Mockito.when(mastercardBinLookupApi.searchBy(Mockito.anyString())).thenReturn(List.of(binResource));

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(new AccountRange("123456"))
                .post("/v1/bin-details/mastercard")
                .then()
                .statusCode(200);
    }
}
