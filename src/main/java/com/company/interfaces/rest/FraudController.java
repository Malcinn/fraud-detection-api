package com.company.interfaces.rest;

import com.company.application.FraudTransactionAssessmentResolver;
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

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

@Path("/v1/fraud")
@ApplicationScoped
public class FraudController {

    @Inject
    private FraudTransactionAssessmentResolver fraudTransactionAssessmentResolver;

    @POST
    @Path("/transaction-assessment")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response transactionAssessment(@Valid TransactionDto transaction) throws UnrecoverableKeyException, CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException {
        return Response.ok(fraudTransactionAssessmentResolver
                .getService(transaction.getType()).processAssessment(transaction), MediaType.APPLICATION_JSON_TYPE).build();
    }
}
