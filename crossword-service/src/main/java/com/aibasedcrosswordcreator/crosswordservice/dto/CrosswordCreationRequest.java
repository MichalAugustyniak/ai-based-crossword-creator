package com.aibasedcrosswordcreator.crosswordservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CrosswordCreationRequest {
    @Size(min = 2, max = 20,
            message = "The theme must be between 3 and 20 characters long.")
    private final String theme;
    @NotBlank(message = "The language must be not blank.")
    private final String language;
    @Size(min = 5, max = 20,
            message = "The height must be between 5 and 20")
    private final int height;
    @Size(min = 5, max = 20,
            message = "The width must be between 5 and 20")
    private final int width;
}
