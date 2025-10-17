package com.aibasedcrosswordcreator.crosswordservice.mapper;

import com.aibasedcrosswordcreator.crosswordservice.dto.StandardCrosswordResponse;
import com.aibasedcrosswordcreator.crosswordservice.dto.WordDTO;
import com.aibasedcrosswordcreator.crosswordservice.model.Clue;
import com.aibasedcrosswordcreator.crosswordservice.model.Coordinates;
import com.aibasedcrosswordcreator.crosswordservice.model.StandardCrossword;
import jakarta.validation.constraints.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class CrosswordMapper {
    public static StandardCrosswordResponse toCrosswordResponse(@NotNull StandardCrossword crossword) {
        List<WordDTO> wordDTOList = new LinkedList<>();
        String[][] body = new String[crossword.getHeight()][crossword.getWidth()];
        for (Coordinates c : crossword.getCoordinates()) {
            Clue clue = c.getClue();
            String text = clue.getWord().getText();
            WordDTO wordDTO = new WordDTO();
            wordDTO.setOrientation(c.getOrientation());
            wordDTO.setClue(clue.getText());
            wordDTO.setText(clue.getWord().getText());
            wordDTO.setCoordinates(new WordDTO.CoordinatesDTO(
                    c.getY(), c.getX()
            ));
            wordDTO.setIdentifier(c.getIdentifier());
            wordDTOList.add(wordDTO);
            if (Objects.equals(c.getOrientation(), "h")) {
                body[c.getY()][c.getX() - 1] = c.getIdentifier();
            }
            if (Objects.equals(c.getOrientation(), "v")) {
                body[c.getY() - 1][c.getX()] = c.getIdentifier();
            }
            for (int i = 0; i < text.length(); i++) {
                if (Objects.equals(c.getOrientation(), "h")) {
                    body[c.getY()][c.getX() + i] = String.valueOf(text.charAt(i));
                } else {
                    body[c.getY() + i][c.getX()] = String.valueOf(text.charAt(i));
                }
            }
        }
        return new StandardCrosswordResponse(
                crossword.getUuid(),
                crossword.getTheme().getName(),
                crossword.getHeight(),
                crossword.getWidth(),
                crossword.getLanguage().getText(),
                crossword.getCreator(),
                body,
                wordDTOList
        );
    }
}
