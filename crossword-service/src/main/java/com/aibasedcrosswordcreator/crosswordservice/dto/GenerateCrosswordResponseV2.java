package com.aibasedcrosswordcreator.crosswordservice.dto;

public record GenerateCrosswordResponseV2(
        String theme,
        int sizeH,
        int sizeW,
        String login,
        String provider,
        String model,
        String language,
        String[][] body,
        String[] clues,
        int[][] coords
) {
}
