package com.company.interfaces.rest;

import com.company.application.MastercardBinLookupApi;
import com.company.interfaces.rest.dto.AccountRange;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Path("/v1/bin-details")
public class BinDetailsController {

    @Inject
    private MastercardBinLookupApi mastercardBinLookupApi;

    @Path("/mastercard")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response binDetails(@Valid AccountRange cardDto) {
        return Response.ok(mastercardBinLookupApi.searchBy(cardDto.getBinNumber()), MediaType.APPLICATION_JSON_TYPE).build();
    }
}
