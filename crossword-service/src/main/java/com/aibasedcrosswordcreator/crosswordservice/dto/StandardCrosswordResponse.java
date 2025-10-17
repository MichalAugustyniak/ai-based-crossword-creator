package com.aibasedcrosswordcreator.crosswordservice.dto;

import java.util.List;
import java.util.UUID;

public record StandardCrosswordResponse(
        UUID uuid,
        String theme,
        int height,
        int width,
        String language,
        String creator,
        String[][] body,
        List<WordDTO> words
) {
}
