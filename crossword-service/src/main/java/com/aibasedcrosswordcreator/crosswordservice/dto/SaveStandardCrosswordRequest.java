package com.aibasedcrosswordcreator.crosswordservice.dto;

import java.util.List;

public record SaveStandardCrosswordRequest(
        String theme,
        String language,
        int height,
        int width,
        List<WordDTO> words
) {
}
