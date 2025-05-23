package com.aibasedcrosswordcreator.crosswordservice.dto;

public record GetCrosswordsRequestDTO(
        int quantity,
        int page,
        Integer height,
        Integer width,
        String provider,
        String model,
        String creator,
        String language
) {
}
