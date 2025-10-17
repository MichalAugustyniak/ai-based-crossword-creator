package com.aibasedcrosswordcreator.crosswordservice.mapper;

import com.aibasedcrosswordcreator.crosswordservice.dto.StandardCrosswordResponse;
import com.aibasedcrosswordcreator.crosswordservice.model.StandardCrossword;

public interface StandardCrosswordResponseMapper {
    StandardCrosswordResponse map(StandardCrossword crossword);

    String type();

    StandardCrosswordResponseMapper newInstance();
}
