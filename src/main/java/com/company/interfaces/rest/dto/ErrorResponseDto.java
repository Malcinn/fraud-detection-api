package com.company.interfaces.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ErrorResponseDto {

    private String status;
    private Object message;
    private String path;
    private LocalDateTime date;
    private String requestId;

}
