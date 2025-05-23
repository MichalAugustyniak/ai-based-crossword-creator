package com.aibasedcrosswordcreator.crosswordservice.dto;

import jakarta.validation.constraints.NotBlank;

public record WordAndClueDTO(
        @NotBlank(message = "The word cannot be blank.")
        String word,
        @NotBlank(message = "The clue cannot be blank.")
        String clue
) {
}
