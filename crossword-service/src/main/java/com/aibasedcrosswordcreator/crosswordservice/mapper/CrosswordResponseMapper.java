package com.aibasedcrosswordcreator.crosswordservice.mapper;

import com.aibasedcrosswordcreator.crosswordservice.dto.CrosswordResponse;
import com.aibasedcrosswordcreator.crosswordservice.model.Crossword;

public interface CrosswordResponseMapper {
    CrosswordResponse map(Crossword crossword);

    String type();

    CrosswordResponseMapper newInstance();
}
