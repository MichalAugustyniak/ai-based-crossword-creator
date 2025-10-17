package com.aibasedcrosswordcreator.crosswordservice.helper;

import com.aibasedcrosswordcreator.crosswordservice.exception.ServerException;
import com.aibasedcrosswordcreator.crosswordservice.generator.CrosswordGenerator;
import com.aibasedcrosswordcreator.crosswordservice.model.*;

import java.sql.Timestamp;
import java.util.*;

public class StandardCrosswordHelper {
    public static StandardCrossword createCrosswordModel(
            com.aibasedcrosswordcreator.crosswordservice.generator.Crossword crossword,
            Theme theme,
            Language language,
            ProviderModel providerModel,
            String type,
            int height,
            int width,
            Map<String, Clue> clueMap,
            String userId
    ) {
        StandardCrossword crosswordModel = new StandardCrossword();
        UUID uuid = UUID.randomUUID();
        crosswordModel.setLanguage(language);
        crosswordModel.setTheme(theme);
        crosswordModel.setHeight(height);
        crosswordModel.setWidth(width);
        crosswordModel.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        crosswordModel.setCreator(userId);
        crosswordModel.setType(type);
        crosswordModel.setProviderModel(providerModel);
        crosswordModel.setUuid(uuid);
        List<Coordinates> coordinatesList = new LinkedList<>();
        for (com.aibasedcrosswordcreator.crosswordservice.generator.Word word : crossword.getWords()) {
            Clue clue = clueMap.get(word.getContent());
            Optional.ofNullable(clue)
                    .orElseThrow(() -> new ServerException("Cannot save the crossword. Missing clue for word: " + word.getContent()));
            Coordinates coordinates = new Coordinates();
            coordinates.setX(word.getStartPoint().wIndex());
            coordinates.setY(word.getStartPoint().hIndex());
            coordinates.setCrossword(crosswordModel);
            coordinates.setClue(clue);
            coordinates.setOrientation(String.valueOf(word.getOrientation().toString().charAt(0)).toLowerCase());
            coordinates.setIdentifier(word.getIdentifier());
            coordinatesList.add(coordinates);
        }
        crosswordModel.getCoordinates().addAll(coordinatesList);
        return crosswordModel;
    }

    public static com.aibasedcrosswordcreator.crosswordservice.generator.Crossword generateCrossword(List<String> words, int height, int width, int iterations, CrosswordGenerator generator) {
        var finalCrossword = generator.generateCrossword(
                words,
                height,
                width,
                "language",
                "theme"
        );
        for (int i = 0; i < iterations; i++) {
            var crossword = generator.generateCrossword(
                    words,
                    height,
                    width,
                    "language",
                    "theme"
            );
            if (crossword.getWords().size() > finalCrossword.getWords().size()) {
                finalCrossword = crossword;
            } else if (crossword.getWords().size() == finalCrossword.getWords().size() && crossword.checkAllSides()) {
                finalCrossword = crossword;
            }
        }
        return finalCrossword;
    }
}
