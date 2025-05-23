package com.aibasedcrosswordcreator.authservice.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenRequestDTO(
        @NotBlank(message = "The username must be not blank.")
        String username,
        @NotBlank(message = "The password must be not blank.")
        String password
) {
}
