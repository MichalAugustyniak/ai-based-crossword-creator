package com.aibasedcrosswordcreator.crosswordservice.helper;

import com.aibasedcrosswordcreator.crosswordservice.model.*;

public class WordHelper {
    public static Word[] buildWords(String[] words, String[] clues, Language language, Theme theme, ProviderModel providerModel) {
        Word[] buildWords = new Word[words.length];
        for (int i = 0; i < buildWords.length; i++) {
            buildWords[i] = new Word();
            buildWords[i].setText(words[i]);
            buildWords[i].getClue().add(Clue.builder().
                    word(buildWords[i]).
                    language(language).
                    theme(theme).
                    text(clues[i].trim())
                    .providerModel(providerModel)
                    .build());
        }
        return buildWords;
    }
}
