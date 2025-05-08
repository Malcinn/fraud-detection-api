package com.company.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionAssessment {

    private int score;

    private String assessment;

    @JsonProperty
    public int getScore() {
        return score;
    }

    @JsonProperty
    public String getAssessment() {
        return assessment;
    }
}
