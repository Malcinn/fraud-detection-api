package com.company.interfaces.rest;

import com.company.application.FraudTransactionAssessmentResolver;
import com.company.interfaces.rest.dto.BinDetailsDto;
import com.company.interfaces.rest.dto.CardDto;
import com.company.interfaces.rest.dto.TransactionDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/v1/fraud")
@ApplicationScoped
public class FraudController {

    @Inject
    private FraudTransactionAssessmentResolver fraudTransactionAssessmentResolver;

    @POST
    @Path("/transaction-assessment")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response transactionAssessment(@Valid TransactionDto transaction) {
        return Response.ok(fraudTransactionAssessmentResolver
                .getService(transaction.getType()).processAssessment(transaction), MediaType.TEXT_PLAIN).build();
    }

    @POST
    @Path("/bin-details")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response transactionAssessment(@Valid CardDto cardDto) {
        return Response.ok(new BinDetailsDto(), MediaType.APPLICATION_JSON_TYPE).build();
    }
}
