package com.aibasedcrosswordcreator.crosswordservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;

public record SaveWordsAndGenerateCluesRequest(
        @NotBlank(message = "The language cannot be blank.")
        String language,
        @NotBlank(message = "The theme cannot be blank.")
        String theme,
        @NotBlank(message = "The provider cannot be blank.")
        String provider,
        @NotBlank(message = "The model cannot be blank.")
        String model,
        @NotNull(message = "The words collection is required.")
        Collection<@NotBlank(message = "The words cannot be blank.")
                String> words
) {
}
