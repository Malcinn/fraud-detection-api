package com.company.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TransactionDto {

    @NotBlank(message = "Bin Number must not be blank")
    @Pattern(regexp = "[0-9]{6,11}", message = "Bin number - the first 6 to 8 digits of a card number")
    private String binNumber;

    @NotNull(message = "Amount must not be null")
    @PositiveOrZero
    private BigDecimal amount;

    @Pattern(regexp = "[A-Z]{3}", message = "Currency must be in alphabetic code. 3 upper case letters")
    @NotBlank(message = "Currency must not be blank")
    private String currency;

    @Pattern(regexp = "[A-Z]{3}", message = "Country must be in Alpha-3 format")
    @NotBlank(message = "Country Alpha3 code must not be blank")
    private String countryAlpha3;

    @NotBlank(message = "Transaction type must be specified, please use one of the following: TRANSFER, DEPOSIT, WITHDRAWAL")
    private String type;
}
