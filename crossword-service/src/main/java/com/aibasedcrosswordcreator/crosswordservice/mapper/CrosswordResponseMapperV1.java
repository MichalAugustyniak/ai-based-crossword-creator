package com.aibasedcrosswordcreator.crosswordservice.mapper;

import com.aibasedcrosswordcreator.crosswordservice.dto.CrosswordResponse;
import com.aibasedcrosswordcreator.crosswordservice.dto.WordDTO;
import com.aibasedcrosswordcreator.crosswordservice.model.Crossword;

import java.util.LinkedList;
import java.util.List;

public class CrosswordResponseMapperV1 implements CrosswordResponseMapper {
    @Override
    public String type() {
        return "V1";
    }

    @Override
    public CrosswordResponseMapper newInstance() {
        return new CrosswordResponseMapperV1();
    }

    @Override
    public CrosswordResponse map(Crossword crossword) {
        String[][] body = new String[crossword.getHeight()][crossword.getWidth()];
        crossword.getCoordinates().forEach(c -> {
            if (c.getOrientation().equals("h")) {
                for (int i = c.getX(); i < c.getX() + c.getClue().getWord().getText().length(); i++) {
                    body[c.getY()][i] = String.valueOf(c.getClue().getWord().getText().charAt(i - c.getX()));
                }
            } else {
                for (int i = c.getY(); i < c.getY() + c.getClue().getWord().getText().length(); i++) {
                    body[i][c.getX()] = String.valueOf(c.getClue().getWord().getText().charAt(i - c.getY()));
                }
            }
        });
        return getCrosswordResponse(crossword, body);
    }

    static CrosswordResponse getCrosswordResponse(Crossword crossword, String[][] body) {
        List<WordDTO> words = new LinkedList<>();
        crossword.getCoordinates().forEach(c -> {
            WordDTO word = new WordDTO();
            word.setText(c.getClue().getWord().getText());
            word.setClue(c.getClue().getText());
            word.setIdentifier(c.getIdentifier());
            word.setOrientation(c.getOrientation());

            if (c.getOrientation().equals("h")) {
                word.setCoordinates(new WordDTO.CoordinatesDTO(
                        c.getY(), c.getX() - 1
                ));
            } else {
                word.setCoordinates(new WordDTO.CoordinatesDTO(
                        c.getY() - 1, c.getX()
                ));
            }
            words.add(word);
        });

        return new CrosswordResponse(
                crossword.getUuid(),
                crossword.getTheme().getName(),
                crossword.getHeight(),
                crossword.getWidth(),
                crossword.getLanguage().getText(),
                crossword.getCreator(),
                body,
                words
        );
    }
}
