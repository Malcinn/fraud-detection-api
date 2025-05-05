package com.company.interfaces.rest.exception;

import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;

@Provider
public class IllegalArgumentExceptionHandler extends ExceptionHandler<IllegalArgumentException> {

    public IllegalArgumentExceptionHandler(UriInfo uriInfo, Request request) {
        super(uriInfo, request);
    }

    @Override
    public Response.Status getStatus() {
        return Response.Status.BAD_REQUEST;
    }

    @Override
    public Object getMessage(IllegalArgumentException exception) {
        return exception.getMessage();
    }

}
