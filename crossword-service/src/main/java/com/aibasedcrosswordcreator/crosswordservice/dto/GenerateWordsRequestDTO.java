package com.aibasedcrosswordcreator.crosswordservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GenerateWordsRequestDTO(
        @NotBlank(message = "The provider must not be blank.") String provider,
        @NotBlank(message = "The model must not be blank.") String model,
        @NotNull(message = "The details are required.") GenerateWordsDetailsDTO details) {
}
