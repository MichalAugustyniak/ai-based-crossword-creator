package com.aibasedcrosswordcreator.crosswordservice.prompt;

import com.aibasedcrosswordcreator.crosswordservice.dto.GenerateWordsDetailsDTO;
import com.aibasedcrosswordcreator.crosswordservice.dto.SaveWordsAndGenerateCluesRequest;

public interface WordPromptBuilder {
    String generateWords(GenerateWordsDetailsDTO dto);

    String generateClues(SaveWordsAndGenerateCluesRequest dto);

    String generateWordsWithClues(GenerateWordsDetailsDTO dto);

    WordPromptParser getParser();
}
