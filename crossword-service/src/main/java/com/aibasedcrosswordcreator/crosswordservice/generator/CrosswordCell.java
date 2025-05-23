package com.aibasedcrosswordcreator.crosswordservice.generator;

import lombok.Data;

@Data
public class CrosswordCell {
    String content = null;
    boolean isWordIdentifier;
    Word owner;
}
