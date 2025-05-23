package com.aibasedcrosswordcreator.configmanagementservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PropertyRequest(
        @NotNull(message = "Application name must be not blank.")
        String applicationName,
        @NotBlank(message = "Application profile must be not blank.")
        String applicationProfile,
        @NotBlank(message = "Property name must be not blank.")
        String propertyName
) {
}
