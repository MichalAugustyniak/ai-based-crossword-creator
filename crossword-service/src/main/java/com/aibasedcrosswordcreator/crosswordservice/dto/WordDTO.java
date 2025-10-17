package com.aibasedcrosswordcreator.crosswordservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WordDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CoordinatesDTO {
        private int hIndex;
        private int wIndex;
    }

    private String text;
    private String identifier;
    private String clue;
    private String orientation;
    private CoordinatesDTO coordinates;
}
