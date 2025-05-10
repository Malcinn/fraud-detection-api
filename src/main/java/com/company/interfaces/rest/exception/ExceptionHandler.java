package com.company.interfaces.rest.exception;

import com.company.interfaces.rest.dto.ErrorResponseDto;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.ext.ExceptionMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public abstract class ExceptionHandler<T extends Throwable> implements ExceptionMapper<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);

    private final UriInfo uriInfo;

    private final Request request;

    private final HttpHeaders headers;

    @Override
    public Response toResponse(T exception) {
        LOGGER.error("Exception {} occurred on: {} {}, message: {}", exception.getClass(), request.getMethod(),
                uriInfo.getPath(), exception.getMessage());
        return Response.status(getStatus(exception))
                .entity(ErrorResponseDto.builder()
                        .status(getStatus(exception).getReasonPhrase())
                        .date(LocalDateTime.now())
                        .path(uriInfo.getPath())
                        .message(getMessage(exception))
                        .requestId(headers.getHeaderString("X-REQUESTID"))
                        .build())
                .build();
    }

    public abstract Response.Status getStatus(T exception);

    public abstract Object getMessage(T exception);

}
