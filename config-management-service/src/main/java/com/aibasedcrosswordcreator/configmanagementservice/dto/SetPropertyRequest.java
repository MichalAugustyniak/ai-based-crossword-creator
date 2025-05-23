package com.aibasedcrosswordcreator.configmanagementservice.dto;

import jakarta.validation.constraints.NotBlank;

public record SetPropertyRequest(
        @NotBlank(message = "The application name must be not blank.")
        String applicationName,
        @NotBlank(message = "The application profile must be not blank.")
        String applicationProfile,
        @NotBlank(message = "The property name must be not blank.")
        String propertyName,
        @NotBlank(message = "The property value must be not blank.")
        String propertyValue
) {
}
