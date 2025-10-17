package com.aibasedcrosswordcreator.crosswordservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GenerateStandardCrosswordRequest(
        @Size(min = 2, max = 20,
                message = "The theme must be between 3 and 20 characters long.")
        String theme,
        @Size(min = 5, max = 20,
                message = "The height must be between 5 and 20")
        int height,
        @Size(min = 5, max = 20,
                message = "The width must be between 5 and 20")
        int width,
        String provider,
        String model,
        @NotBlank(message = "The language must be not blank.")
        String language,
        @NotBlank(message = "The type must be not blank.")
        String type
) {
}
