package com.aibasedcrosswordcreator.crosswordservice.mapper;

import com.aibasedcrosswordcreator.crosswordservice.dto.CrosswordResponse;
import com.aibasedcrosswordcreator.crosswordservice.model.Crossword;

import java.util.Optional;

public class CrosswordResponseMapperV2 implements CrosswordResponseMapper {
    @Override
    public String type() {
        return "V2";
    }

    @Override
    public CrosswordResponseMapper newInstance() {
        return new CrosswordResponseMapperV2();
    }

    @Override
    public CrosswordResponse map(Crossword crossword) {
        String[][] body = new String[crossword.getHeight()][crossword.getWidth()];
        crossword.getCoordinates().forEach(c -> {
            if (c.getOrientation().equals("h")) {
                body[c.getY()][c.getX() - 1] = Optional.ofNullable(c.getIdentifier()).orElse("");
                for (int i = c.getX(); i < c.getX() + c.getClue().getWord().getText().length(); i++) {
                    body[c.getY()][i] = String.valueOf(c.getClue().getWord().getText().charAt(i - c.getX()));
                }
            } else {
                body[c.getY() - 1][c.getX()] = Optional.ofNullable(c.getIdentifier()).orElse("");
                for (int i = c.getY(); i < c.getY() + c.getClue().getWord().getText().length(); i++) {
                    body[i][c.getX()] = String.valueOf(c.getClue().getWord().getText().charAt(i - c.getY()));
                }
            }
        });
        return CrosswordResponseMapperV1.getCrosswordResponse(crossword, body);
    }
}
