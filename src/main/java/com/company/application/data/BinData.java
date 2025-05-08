package com.company.application.data;

import io.quarkus.arc.properties.IfBuildProperty;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BinData {
    private String binNum;
    private String countryAlpha3;
}
