package com.calculator.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(
        @NotBlank(message = "The username is obligatory") String username,
        @NotBlank(message = "The password is obligatory") String password
) {
}
