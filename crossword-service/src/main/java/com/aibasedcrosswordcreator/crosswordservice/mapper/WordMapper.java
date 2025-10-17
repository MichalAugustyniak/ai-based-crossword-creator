package com.aibasedcrosswordcreator.crosswordservice.mapper;

import com.aibasedcrosswordcreator.crosswordservice.dto.WordDTO;
import com.aibasedcrosswordcreator.crosswordservice.model.Coordinates;

import java.util.List;

public class WordMapper {
    public static List<WordDTO> mapToWordList(List<Coordinates> coordinates) {
        return coordinates.stream().map(c -> new WordDTO(
                c.getClue().getWord().getText(),
                c.getIdentifier(),
                c.getClue().getText(),
                c.getOrientation(),
                new WordDTO.CoordinatesDTO(c.getY(), c.getX())
        )).toList();
    }
}
