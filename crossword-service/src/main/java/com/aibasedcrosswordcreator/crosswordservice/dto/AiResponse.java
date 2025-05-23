package com.aibasedcrosswordcreator.crosswordservice.dto;


import jakarta.validation.constraints.Size;

public record AiResponse(

        @Size(min = 5, max = 500)
        String message,
        String provider,
        String model) {
}
