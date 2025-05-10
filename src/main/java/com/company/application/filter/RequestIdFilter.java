package com.company.application.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.UUID;

@Provider
public class RequestIdFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final String HEADER_NAME = "X-REQUESTID";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String requestId = requestContext.getHeaderString(HEADER_NAME);
        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();

        }

        requestContext.getHeaders().add("X-REQUESTID",requestId);
        // Store it for access in logs, services, etc.
        MDC.put(HEADER_NAME, requestId);

        // Also put it in request context properties (optional)
        requestContext.setProperty(HEADER_NAME, requestId);
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        String requestId = (String) requestContext.getProperty(HEADER_NAME);
        if (requestId != null) {
            responseContext.getHeaders().add(HEADER_NAME, requestId); // echo it back
        }
        // Clear it after request to avoid memory leaks in MDC
        MDC.remove(HEADER_NAME);
    }
}
