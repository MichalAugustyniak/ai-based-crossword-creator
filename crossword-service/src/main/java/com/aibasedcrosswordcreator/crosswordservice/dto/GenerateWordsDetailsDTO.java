package com.aibasedcrosswordcreator.crosswordservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record GenerateWordsDetailsDTO(
        @Size(min = 3, max = 20)
        String theme,
        @Size(min = 5, max = 15)
        String language,
        @Min(1)
        int quantity,
        @Min(1)
        @Max(15)
        int maxLength
) {
}
