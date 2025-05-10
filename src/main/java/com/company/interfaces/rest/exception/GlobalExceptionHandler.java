package com.company.interfaces.rest.exception;

import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionHandler extends ExceptionHandler<Throwable> {

    public GlobalExceptionHandler(UriInfo uriInfo, Request request, HttpHeaders headers) {
        super(uriInfo, request, headers);
    }

    @Override
    public Response.Status getStatus(Throwable exception) {
        return Response.Status.INTERNAL_SERVER_ERROR;
    }

    @Override
    public Object getMessage(Throwable exception) {
        return exception.getMessage();
    }
}
