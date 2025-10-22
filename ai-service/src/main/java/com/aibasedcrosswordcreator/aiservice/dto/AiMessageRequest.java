package com.aibasedcrosswordcreator.aiservice.dto;

import jakarta.validation.constraints.NotBlank;

public record AiMessageRequest(
        @NotBlank(message = "The message must be not blank.")
        String message,
        @NotBlank(message = "The provider must be not blank.")
        String provider,
        @NotBlank(message = "The model must be not blank.")
        String model
) {
}
