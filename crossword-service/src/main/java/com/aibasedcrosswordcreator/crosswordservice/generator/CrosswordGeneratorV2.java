package com.aibasedcrosswordcreator.crosswordservice.generator;

import java.util.List;

public class CrosswordGeneratorV2 implements CrosswordGenerator {
    @Override
    public Crossword generateCrossword(List<String> words, int height, int width, String language, String theme) {
        assert height > 1 && width > 1;
        var generator = new CrosswordGeneratorV1();
        var crossword = generator.generateCrossword(words, height + 1, width + 1, language, theme);
        for (var word : crossword.getWords()) {
            word.setStartPoint(new Point(word.getStartPoint().hIndex() - 1, word.getStartPoint().wIndex() - 1));
        }
        crossword.setBody(removeIdentifiersAndTrim(crossword.getBody()));
        return crossword;
    }

    private CrosswordCell[][] removeIdentifiersAndTrim(CrosswordCell[][] crosswordBody) {
        var height = crosswordBody.length;
        assert height > 1;
        var width = crosswordBody[0].length;
        assert width > 1;
        CrosswordCell[][] body = new CrosswordCell[height - 1][width - 1];
        for (int i = 0; i < height - 1; i++) {
            for (int j = 0; j < width - 1; j++) {
                body[i][j] = crosswordBody[i + 1][j + 1];
                if (body[i][j].isWordIdentifier()) {
                    body[i][j].setContent(null);
                }
            }
        }
        return body;
    }
}
