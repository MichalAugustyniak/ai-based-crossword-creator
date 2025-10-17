package com.aibasedcrosswordcreator.crosswordservice.dto;

import lombok.Getter;

@Getter
public class CrosswordAiCreationRequest extends CrosswordCreationRequest {
    private final String provider;
    private final String model;

    public CrosswordAiCreationRequest(String theme, String language, int height, int width, String provider, String model) {
        super(theme, language, height, width);
        this.provider = provider;
        this.model = model;
    }
}
