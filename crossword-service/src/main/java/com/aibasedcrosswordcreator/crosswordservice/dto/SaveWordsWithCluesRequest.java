package com.aibasedcrosswordcreator.crosswordservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SaveWordsWithCluesRequest(
        @NotBlank(message = "The theme cannot be blank.")
        String theme,
        @NotBlank(message = "The language cannot be blank.")
        String language,
        String provider,
        String model,
        @NotNull(message = "The word and clues collection is required.")
        List<@NotNull WordAndClueDTO> wordsAndClues) {
}
