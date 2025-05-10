package com.company.interfaces.rest;

import com.company.application.BinResourcesSynchronizationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/v1/synchronization")
public class SynchronizationController {

    @Inject
    private BinResourcesSynchronizationService service;

    @Path("/binResource")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response triggerSynchronization() {
        service.synchronize();
        return Response.ok("Synchronization triggered successfully", MediaType.TEXT_PLAIN).build();
    }
}
