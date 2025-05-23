package com.aibasedcrosswordcreator.crosswordservice.prompt;

import com.aibasedcrosswordcreator.crosswordservice.dto.WordAndClueDTO;

import java.util.List;

public interface WordPromptParser {
    List<String> parseGeneratedWordsToList(String textContainingWords);

    List<String> parseGeneratedCluesToList(String textContainingClues);

    List<WordAndClueDTO> parseGeneratedWordsWithClues(String textContainingWordsWithClues);
}
