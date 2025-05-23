package com.aibasedcrosswordcreator.crosswordservice.generator;

import java.util.*;

public class CrosswordGeneratorV1 implements CrosswordGenerator {
    @Override
    public Crossword generateCrossword(List<String> words, int height, int width, String language, String theme) {
        List<String> wordsCopy = new LinkedList<>(words);
        CrosswordCell[][] body = new CrosswordCell[height][width];
        Crossword crossword = new Crossword(body, theme);
        crossword.setLanguage(language);
        crossword.setTheme(theme);

        for (int i = 0; i < crossword.getBody().length; i++) {
            for (int j = 0; j < crossword.getBody()[0].length; j++) {
                crossword.getBody()[i][j] = new CrosswordCell();
            }
        }

        List<String> wordsThatCanBePlacedHorizontally = new LinkedList<>(Arrays.asList(wordsCopy.stream().filter(w -> w.length() <= width - 1).toArray(String[]::new)));
        Collections.shuffle(wordsThatCanBePlacedHorizontally);

        // put first word
        for (int i = 0; i < wordsThatCanBePlacedHorizontally.size(); i++) {
            try {
                String wordToPlace = wordsThatCanBePlacedHorizontally.get(i);
                Point firstWordStartPoint = new Point(height / 2, 1);
                crossword.placeWordHorizontally(wordToPlace.toLowerCase(), 1, firstWordStartPoint);
                //wordsThatCanBePlacedHorizontally.remove(i);
                wordsCopy.removeIf(w -> w.equals(wordToPlace));
                //Collections.shuffle(words);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        if (crossword.getWords().isEmpty()) {
            throw new RuntimeException("No first word placed in the crossword");
        }
        Collections.shuffle(wordsCopy);
        int i = 0;
        int end = (int) Math.pow(wordsCopy.size(), 2);
        while (i < end) {
            if (wordsCopy.isEmpty()) {
                break;
            }
            try {
                crossword.printBody();
                crossword.findPlaceAndPutWord(wordsCopy.get(0).toLowerCase(), true);
                wordsCopy.remove(0);
            } catch (CannotPlaceWordException e) {
                //System.out.println(e.getMessage());
                String wordToPlace = wordsCopy.get(0);
                wordsCopy.remove(0);
                wordsCopy.add(wordToPlace);
            }
            i++;
        }

        return crossword;
    }
}
