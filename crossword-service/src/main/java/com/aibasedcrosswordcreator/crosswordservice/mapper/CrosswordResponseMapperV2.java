package com.aibasedcrosswordcreator.crosswordservice.mapper;

import com.aibasedcrosswordcreator.crosswordservice.dto.StandardCrosswordResponse;
import com.aibasedcrosswordcreator.crosswordservice.model.StandardCrossword;

public class CrosswordResponseMapperV2 implements StandardCrosswordResponseMapper {
    @Override
    public String type() {
        return "V2";
    }

    @Override
    public StandardCrosswordResponseMapper newInstance() {
        return new CrosswordResponseMapperV2();
    }

    @Override
    public StandardCrosswordResponse map(StandardCrossword crossword) {
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
        return CrosswordResponseMapperV1.getCrosswordResponse(crossword, body);
    }
}
