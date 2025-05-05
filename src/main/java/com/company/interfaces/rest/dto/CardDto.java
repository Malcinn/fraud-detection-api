package com.company.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CardDto {

    @NotBlank(message = "Bin Number must not be blank")
    @Pattern(regexp = "[0-9]{6,8}", message = "Bin number - the first 6 to 8 digits of a card number")
    private String binNumber;
}
