package com.company.interfaces.rest.exception;

import com.company.application.exception.MastercardBinLookupApiException;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;
import org.openapitools.client.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class MastercardBinLookupApiExceptionHandler extends ExceptionHandler<MastercardBinLookupApiException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MastercardBinLookupApiExceptionHandler.class);

    public MastercardBinLookupApiExceptionHandler(UriInfo uriInfo, Request request) {
        super(uriInfo, request);
    }

    @Override
    public Response.Status getStatus(MastercardBinLookupApiException exception) {
        return Response.Status.INTERNAL_SERVER_ERROR;
    }

    @Override
    public Object getMessage(MastercardBinLookupApiException exception) {
        return exception.getMessage();
    }

    @Override
    public Response toResponse(MastercardBinLookupApiException exception) {
        LOGGER.error("Message: {}", exception.getMessage());
        LOGGER.error("Status code: {}", exception.getCode());
        LOGGER.error("Reason: {}", exception.getResponseBody());
        LOGGER.error("Response headers: {}", exception.getResponseHeaders());
        return super.toResponse(exception);
    }


}
