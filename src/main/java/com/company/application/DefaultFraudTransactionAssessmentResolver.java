package com.company.application;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class DefaultFraudTransactionAssessmentResolver implements FraudTransactionAssessmentResolver {

    private final List<FraudTransactionAssessmentService> services;

    @Override
    public FraudTransactionAssessmentService getService(String type) {
        return services.stream()
                .filter(service -> type.equalsIgnoreCase(service.getType()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No Service defined for type: " + type));
    }
}
