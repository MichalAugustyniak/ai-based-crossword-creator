package com.aibasedcrosswordcreator.crosswordservice.generator;

import java.util.List;

public interface CrosswordGenerator {
    Crossword generateCrossword(List<String> words, int height, int width, String language, String theme);
}
