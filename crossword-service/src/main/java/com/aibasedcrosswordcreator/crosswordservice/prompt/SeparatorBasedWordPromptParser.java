package com.aibasedcrosswordcreator.crosswordservice.prompt;

import com.aibasedcrosswordcreator.crosswordservice.dto.WordAndClueDTO;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SeparatorBasedWordPromptParser implements WordPromptParser {
    @Override
    public List<String> parseGeneratedWordsToList(String textContainingWords) {
        return Arrays.stream(textContainingWords.replace(" ", "").split(",")).toList();
    }

    @Override
    public List<String> parseGeneratedCluesToList(String textContainingClues) {
        return Arrays.stream(textContainingClues.split("/-=-/")).toList();
    }

    @Override
    public List<WordAndClueDTO> parseGeneratedWordsWithClues(String textContainingWordsWithClues) {
        List<String> wordCluesPairs = Arrays.stream(textContainingWordsWithClues.split("/-=-/")).toList();
        List<WordAndClueDTO> wordsAndClues = new LinkedList<>();
        wordCluesPairs.forEach(w -> {
            String[] split = w.split("/#=#/");
            wordsAndClues.add(new WordAndClueDTO(split[0], split[1]));
        });
        return wordsAndClues;
    }
}
