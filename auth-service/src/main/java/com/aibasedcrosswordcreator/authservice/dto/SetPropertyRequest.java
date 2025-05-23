package com.aibasedcrosswordcreator.authservice.dto;

import jakarta.validation.constraints.NotBlank;

public record SetPropertyRequest(
        @NotBlank(message = "The property name must be not blank.")
        String propertyName,
        @NotBlank(message = "The property value must be not blank.")
        String propertyValue
) {
}
