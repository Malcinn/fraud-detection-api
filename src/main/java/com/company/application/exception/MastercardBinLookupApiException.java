package com.company.application.exception;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class MastercardBinLookupApiException extends RuntimeException {

    private int code = 0;
    private Map<String, List<String>> responseHeaders = null;
    private String responseBody = null;

    public MastercardBinLookupApiException(String message, Throwable throwable, int code, Map<String, List<String>> responseHeaders, String responseBody) {
        super(message, throwable);
        this.code = code;
        this.responseHeaders = responseHeaders;
        this.responseBody = responseBody;
    }

}
