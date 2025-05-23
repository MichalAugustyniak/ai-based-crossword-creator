package com.aibasedcrosswordcreator.crosswordservice.generator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Word {
    WordOrientation orientation;
    Point startPoint;
    String content = null;
    String identifier;
}
