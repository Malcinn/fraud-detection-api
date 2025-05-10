package com.company.interfaces.rest.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationExceptionHandler extends ExceptionHandler<ConstraintViolationException> {

    public ConstraintViolationExceptionHandler(UriInfo uriInfo, Request request, HttpHeaders headers) {
        super(uriInfo, request, headers);
    }

    @Override
    public Response.Status getStatus(ConstraintViolationException exception) {
        return Response.Status.BAD_REQUEST;
    }

    @Override
    public Object getMessage(ConstraintViolationException exception) {
        return exception.getConstraintViolations().stream()
                .map(constraintViolation -> constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage())
                .toList();
    }
}
